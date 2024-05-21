package com.example.nam.screen

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
import com.example.nam.storage.CacheRepository
import com.example.nam.storage.dto.MetricaCounterResponseDto
import com.example.nam.storage.dto.Website
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun WebsitesScreen() {

    val websitesJson = CacheRepository.get(CacheRepository.CacheType.WEBSITES)
    val type = object : TypeToken<List<Website>>() {}.type
    val websites = Gson().fromJson<List<Website>>(websitesJson, type)

    Column(modifier = Modifier.padding(16.dp)) {
        websites?.forEach { website ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = website.name)
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(text = stringResource(id = R.string.retrieve))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = website.counterId.toString())
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { sendWebsitesToEmail(websites) },
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = stringResource(id = R.string.send_to_email))
        }
    }
}

private fun sendWebsitesToEmail(websites: List<Website>) {

}