package com.example.nam

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nam.screen.MainScreen
import com.example.nam.service.MetricaRestClient
import com.example.nam.service.MetricaRestClient.Companion.YANDEX_METRICA_TAG
import com.example.nam.storage.CacheRepository
import com.example.nam.theme.AppTheme
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var metricaToken: String
        lateinit var self: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        authenticateYandexApi()
        self = this
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }

    private fun authenticateYandexApi() {
        Log.d(YANDEX_METRICA_TAG, "Enter auth")
        val sdk = YandexAuthSdk.create(YandexAuthOptions(this))
        Log.d(YANDEX_METRICA_TAG, sdk.toString())
        val launcher = registerForActivityResult(sdk.contract) { result ->
            Log.d(YANDEX_METRICA_TAG, result.toString())
            when (result) {
                is YandexAuthResult.Success -> {
                    Log.d(YANDEX_METRICA_TAG, "Подключение успешно")
                    metricaToken = sdk.getJwt(result.token)
                }
                is YandexAuthResult.Failure -> {
                    Log.e(YANDEX_METRICA_TAG, "Ошибка подключения! Подробности: ${result.exception}")
                    CacheRepository.put(CacheRepository.CacheType.NOTIFICATION, result.exception.toString())
                }
                YandexAuthResult.Cancelled -> {
                    Log.d(YANDEX_METRICA_TAG, "Запрос отменён")
                }
                else -> {}
            }
        }
        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
    }
}
