package com.example.nam.service

import android.util.Log
import com.example.nam.MainActivity
import com.example.nam.storage.ErrorViewModel
import com.example.nam.storage.dto.Website
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

object CsvReportService {

    private const val REPORTER_TAG = "[REPORTER]"

    suspend fun generateWebsitesCsvReport(
        websites: List<Website>,
        errorViewModel: ErrorViewModel,
        successHandler: (File) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val file = File(
                    MainActivity.self.filesDir,
                    "nam_websites_report.csv")

                file.createNewFile()

                val fileWriter = OutputStreamWriter(
                    FileOutputStream(file),
                    Charsets.UTF_8
                )
                fileWriter.write(buildCsvFile(websites))
                fileWriter.flush()
                fileWriter.close()

                Log.d(REPORTER_TAG, "CSV отчёт сохранен: ${file.absolutePath}")
                successHandler(file)
            } catch (e: IOException) {
                errorViewModel.setErrorMessage("Ошибка сохранения CSV отчёта!")
                Log.e(REPORTER_TAG, "Ошибка сохранения CSV отчёта, подробности: ${e.message}")
            }
        }
    }

    private fun buildCsvFile(websites: List<Website>): String {
        val sb = StringBuilder()
        for (website in websites) {
            sb.append(
                "${website.name};${website.activity}${System.lineSeparator()}"
            )
        }
        return sb.toString()
    }

}