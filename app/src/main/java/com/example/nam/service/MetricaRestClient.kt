package com.example.nam.service

import android.util.Log
import com.example.nam.MainActivity
import com.example.nam.storage.dto.MetricaCounterDto
import com.example.nam.storage.dto.MetricaCounterResponseDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class MetricaRestClient {

    companion object {
        const val YANDEX_METRICA_TAG = "[YANDEX-METRICA]"
        const val COUNTERS_REQUEST_URL = "https://api-metrika.yandex.net/management/v1/counters"
        const val COUNTER_REQUEST_URL = "https://api-metrika.yandex.net/management/v1/counter/"
    }

    private fun makeAuthenticatedRequest(handleResponse: (YandexAuthToken) -> Unit, handleErrorResponse: (YandexAuthException) -> Unit) {
        val sdk = YandexAuthSdk.create(YandexAuthOptions(MainActivity.self.baseContext))
        val launcher = MainActivity.self.registerForActivityResult(sdk.contract) { result ->
            when (result) {
                is YandexAuthResult.Success -> {
                    Log.d(YANDEX_METRICA_TAG, "Подключение успешно")
                    handleResponse(result.token)
                }
                is YandexAuthResult.Failure -> {
                    Log.e(YANDEX_METRICA_TAG, "Ошибка подключения! Подробности: ${result.exception}")
                    handleErrorResponse(result.exception)
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

    fun createCounter(metricaCounterDto: MetricaCounterDto, handleResponse: () -> Unit, handleError: (Any) -> Unit) {
        makeAuthenticatedRequest({
            makeCreateCounterRequest(metricaCounterDto, handleResponse, handleError)
        }, {
            handleError(it)
        })
    }

    fun getAllCountersInfo(handleResponse: (List<MetricaCounterResponseDto>) -> Unit, handleError: (Any) -> Unit) {
        makeAuthenticatedRequest({
            makeGetAllCountersRequest(handleResponse, handleError)
        }, {
            handleError(it)
        })
    }

    fun getCounterInfo(counterId: Long, handleResponse: (MetricaCounterResponseDto) -> Unit, handleError: (Any) -> Unit) {
        makeAuthenticatedRequest({
            makeCounterByIdRequest(COUNTER_REQUEST_URL + "$counterId", handleResponse, handleError)
        }, {
            handleError(it)
        })
    }

    private fun makeCreateCounterRequest(bodyObject: MetricaCounterDto, handleResponse: () -> Unit, handleError: (exception: IOException) -> Unit) {
        val client = OkHttpClient()

        val body = Gson().toJson(bodyObject).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(COUNTERS_REQUEST_URL)
            .method("POST", body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse()
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.w(YANDEX_METRICA_TAG, "Произошла ошибка запроса на создание счётчика! Подробности: ${e.message}")
                handleError(e)
            }
        })
    }

    private fun makeCounterByIdRequest(url: String, handleResponse: (responseBody: MetricaCounterResponseDto) -> Unit, handleError: (exception: IOException) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                handleResponse(Gson().fromJson(responseBody, MetricaCounterResponseDto::class.java))
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.w(YANDEX_METRICA_TAG, "Произошла ошибка запроса на создание счётчика! Подробности: ${e.message}")
                handleError(e)
            }
        })
    }

    private fun makeGetAllCountersRequest(handleResponse: (responseBody: List<MetricaCounterResponseDto>) -> Unit, handleError: (exception: IOException) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(COUNTERS_REQUEST_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val type = object : TypeToken<List<MetricaCounterResponseDto>>() {}.type
                handleResponse(Gson().fromJson(responseBody, type))
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.w(YANDEX_METRICA_TAG, "Произошла ошибка запроса на создание счётчика! Подробности: ${e.message}")
                handleError(e)
            }
        })
    }
}