package com.example.nam.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nam.R
import com.example.nam.service.CsvReportService
import com.example.nam.service.EmailService
import com.example.nam.service.EmailService.EMAIL_TAG
import com.example.nam.storage.EmailRepository
import com.example.nam.storage.ErrorViewModel
import com.example.nam.storage.WebsiteRepository
import com.example.nam.storage.WebsiteService
import com.example.nam.storage.dto.Website
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WebsitesScreen(errorViewModel: ErrorViewModel) {
    val allWebsites = WebsiteRepository.find()
    Column(modifier = Modifier.padding(16.dp)) {
        allWebsites.forEach { website ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                website.name?.let { Text(text = it) }
                Button(
                    onClick = { WebsiteService.getByCounterId(website.counterId, errorViewModel) },
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(text = stringResource(id = R.string.retrieve))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = website.activity.toString())
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { CoroutineScope(Dispatchers.IO).launch {
                sendReport(allWebsites, errorViewModel)
            }  },
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = stringResource(id = R.string.send_to_email))
        }
    }
}

private suspend fun sendReport(websites: List<Website>, errorViewModel: ErrorViewModel) {
    val setting = EmailRepository.find()
    if (setting == null) {
        Log.w(EMAIL_TAG, "Ошибка отправки отчёта, нет сохраненных настроек SMTP-сервера")
        errorViewModel.setErrorMessage("Перед отправлением отчёта сохраните настройки SMPT-сервера!")
        return
    }
    CsvReportService.generateWebsitesCsvReport(websites, errorViewModel) {
        CoroutineScope(Dispatchers.IO).launch { EmailService.sendEmail(setting, it, errorViewModel) }
    }
}