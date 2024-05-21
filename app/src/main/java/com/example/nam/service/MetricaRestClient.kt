package com.example.nam.service

import android.util.Log
import com.example.nam.MainActivity
import com.example.nam.storage.dto.MetricaCounterDto
import com.example.nam.storage.dto.MetricaCounterInfoDto
import com.example.nam.storage.dto.MetricaCounterResponseDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    fun createCounter(metricaCounterDto: MetricaCounterDto, handleResponse: () -> Unit, handleError: (Any) -> Unit) {
        makeCreateCounterRequest(metricaCounterDto, handleResponse, handleError)
    }

    fun getAllCountersInfo(handleResponse: (MetricaCounterInfoDto) -> Unit, handleError: (Any) -> Unit) {
        makeGetAllCountersRequest(handleResponse, handleError)
    }

    fun getCounterInfo(counterId: Long, handleResponse: (MetricaCounterResponseDto) -> Unit, handleError: (Any) -> Unit) {
        makeCounterByIdRequest(COUNTER_REQUEST_URL + "$counterId", handleResponse, handleError)
    }

    private fun makeCreateCounterRequest(bodyObject: MetricaCounterDto, handleResponse: () -> Unit, handleError: (exception: IOException) -> Unit) {
        val client = OkHttpClient()

        val body = Gson().toJson(bodyObject).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(COUNTERS_REQUEST_URL)
            .method("POST", body)
            .addHeader("Authorization", "Bearer ${MainActivity.metricaToken}")
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
            .addHeader("Authorization", "Bearer ${MainActivity.metricaToken}")
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

    private fun makeGetAllCountersRequest(handleResponse: (responseBody: MetricaCounterInfoDto) -> Unit, handleError: (exception: IOException) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(COUNTERS_REQUEST_URL)
            .addHeader("Authorization", "Bearer ${MainActivity.metricaToken}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val metricaCounterInfoDto = Gson().fromJson(responseBody, MetricaCounterInfoDto::class.java)
                if (metricaCounterInfoDto.counters == null) {
                    handleResponse(MetricaCounterInfoDto(0, listOf()))
                } else {
                    handleResponse(metricaCounterInfoDto)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.w(YANDEX_METRICA_TAG, "Произошла ошибка запроса на создание счётчика! Подробности: ${e.message}")
                handleError(e)
            }
        })
    }
}