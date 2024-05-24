package com.example.nam.service

import android.util.Log
import android.util.Range
import com.example.nam.storage.TokenRepository
import com.example.nam.storage.dto.MetricaCounterInfoDto
import com.example.nam.storage.dto.MetricaCounterResponseDto
import com.example.nam.storage.dto.MetricaPageViewsResponseDto
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MetricaRestClient {

    companion object {
        const val YANDEX_METRICA_TAG = "[YANDEX-METRICA]"
        const val COUNTERS_REQUEST_URL = "https://api-metrika.yandex.net/management/v1/counters"
        const val COUNTER_REQUEST_URL = "https://api-metrika.yandex.net/management/v1/counter/"
        const val STAT_PAGE_VIEWS_ONE_WEEK_REQUEST_URL =
            "https://api-metrika.yandex.net/stat/v1/data?ids=%s&metrics=ym:s:users"
    }

    fun getAllCountersInfo(
        handleResponse: (MetricaCounterInfoDto) -> Unit,
        handleError: (Any) -> Unit
    ) {
        makeGetAllCountersRequest(handleResponse, handleError)
    }

    fun getCounterInfo(
        counterId: Long,
        handleResponse: (MetricaCounterResponseDto) -> Unit,
        handleError: (Any) -> Unit
    ) {
        makeCounterByIdRequest(counterId, handleResponse, handleError)
    }

    fun getOneWeekPageViewsByCounterId(
        counterId: Long,
        handleResponse: (MetricaPageViewsResponseDto) -> Unit,
        handleError: (Any) -> Unit
    ) {
        makeGetOneWeekPageViewsByCounterIdRequest(counterId, handleResponse, handleError)
    }

    private fun makeCounterByIdRequest(
        counterId: Long,
        handleResponse: (responseBody: MetricaCounterResponseDto) -> Unit,
        handleError: (exception: IOException) -> Unit
    ) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(COUNTER_REQUEST_URL + "$counterId")
            .addHeader("Authorization", "Bearer ${TokenRepository.find()?.token}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    handleResponse(Gson().fromJson(responseBody, MetricaCounterResponseDto::class.java))
                } else if (!Range.create(200, 299).contains(response.code)) {
                    Log.w(
                        YANDEX_METRICA_TAG,
                        "Возникла ошибка выполнения запроса на получения информации о счётчике номер $counterId. Код ответа: ${response.code}"
                    )
                    handleError(IOException("Ошибка выполнения запроса на получения информации о счётчике номер $counterId. Код ответа: ${response.code}"))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.w(
                    YANDEX_METRICA_TAG,
                    "Произошла ошибка запроса на создание счётчика! Подробности: ${e.message}"
                )
                handleError(e)
            }
        })
    }

    private fun makeGetAllCountersRequest(
        handleResponse: (responseBody: MetricaCounterInfoDto) -> Unit,
        handleError: (exception: IOException) -> Unit
    ) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(COUNTERS_REQUEST_URL)
            .addHeader("Authorization", "Bearer ${TokenRepository.find()?.token}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val metricaCounterInfoDto =
                    Gson().fromJson(responseBody, MetricaCounterInfoDto::class.java)
                if (metricaCounterInfoDto.counters == null) {
                    handleResponse(MetricaCounterInfoDto(0, listOf()))
                } else if (!Range.create(200, 299).contains(response.code)) {
                    Log.w(
                        YANDEX_METRICA_TAG,
                        "Возникла ошибка выполнения запроса на получение всех счётчиков. Код ответа: ${response.code}"
                    )
                    handleError(IOException("Ошибка выполнения запроса получения всех счётчиков. Код: ${response.code}"))
                } else {
                    handleResponse(metricaCounterInfoDto)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.w(
                    YANDEX_METRICA_TAG,
                    "Произошла ошибка запроса на создание счётчика! Подробности: ${e.message}"
                )
                handleError(e)
            }
        })
    }

    private fun makeGetOneWeekPageViewsByCounterIdRequest(
        counterId: Long,
        handleResponse: (MetricaPageViewsResponseDto) -> Unit,
        handleError: (Any) -> Unit
    ) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(String.format(STAT_PAGE_VIEWS_ONE_WEEK_REQUEST_URL, "$counterId"))
            .addHeader("Authorization", "Bearer ${TokenRepository.find()?.token}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val metricaCounterInfoDto =
                    Gson().fromJson(responseBody, MetricaPageViewsResponseDto::class.java)
                if (metricaCounterInfoDto.totals == null) {
                    handleResponse(MetricaPageViewsResponseDto(0))
                } else if (!Range.create(200, 299).contains(response.code)) {
                    Log.w(
                        YANDEX_METRICA_TAG,
                        "Возникла ошибка выполнения запроса на получения количества посещений сайта. Код ответа: ${response.code}"
                    )
                    handleError(IllegalArgumentException("Ошибка выполнения запроса на получение количества посещений сайта. Код: ${response.code}"))
                } else {
                    handleResponse(metricaCounterInfoDto)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.w(
                    YANDEX_METRICA_TAG,
                    "Произошла ошибка запроса посещаемости счётчика! Подробности: ${e.message}"
                )
                handleError(e)
            }
        })
    }

}