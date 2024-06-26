package com.example.nam

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.nam.screen.MainScreen
import com.example.nam.service.MetricaRestClient.Companion.YANDEX_METRICA_TAG
import com.example.nam.storage.TokenRepository
import com.example.nam.storage.WebsiteRepository
import com.example.nam.storage.dto.YandexToken
import com.example.nam.theme.AppTheme
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var tokenState: MutableState<TokenState> = mutableStateOf(TokenState.PENDING)

    companion object {
        lateinit var self: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        self = this
        handleToken()
        setContent {
            AppTheme {
                when (tokenState.value) {
                    TokenState.PENDING -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    TokenState.SUCCESS -> {
                        MainScreen()
                    }
                    else -> {
                        AlertDialog(
                            onDismissRequest = { finish() },
                            title = { Text("Ошибка") },
                            text = { Text("Возникла ошибка получения токена Yandex API") },
                            confirmButton = {
                                Button(
                                    onClick = { finish() }
                                ) {
                                    Text("Закрыть")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun authenticateYandexApi() {
        val sdk = YandexAuthSdk.create(YandexAuthOptions(baseContext))
        val launcher = registerForActivityResult(sdk.contract) { result ->
            CoroutineScope(Dispatchers.IO).launch {
                when (result) {
                    is YandexAuthResult.Success -> {
                        Log.d(YANDEX_METRICA_TAG, "Подключение успешно");
                        TokenRepository.save(YandexToken(result.token.value, result.token.expiresIn, System.currentTimeMillis()))
                        tokenState.value = TokenState.SUCCESS
                    }
                    is YandexAuthResult.Failure -> {
                        Log.e(YANDEX_METRICA_TAG, "Ошибка подключения! Подробности: ${result.exception}")
                        tokenState.value = TokenState.FAILURE
                    }
                    YandexAuthResult.Cancelled -> {
                        Log.d(YANDEX_METRICA_TAG, "Запрос отменён")
                        tokenState.value = TokenState.FAILURE
                    }
                    else -> {}
                }
            }
        }
        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
    }

    private fun handleToken() {
        val yandexToken = TokenRepository.find()
        if (yandexToken == null
            || yandexToken.createdIn + yandexToken.expiresIn * 1000L <= System.currentTimeMillis()) {
            authenticateYandexApi()
        } else {
            tokenState.value = TokenState.SUCCESS
        }
    }

    enum class TokenState {
        PENDING, SUCCESS, FAILURE
    }

}
