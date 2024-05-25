package com.example.nam.service

import android.util.Log
import com.example.nam.service.MetricaRestClient.Companion.YANDEX_METRICA_TAG
import com.example.nam.storage.ErrorViewModel
import com.example.nam.storage.WebsiteRepository
import com.example.nam.storage.dto.Website

object WebsiteService {

    fun addWebsite(website: Website, errorViewModel: ErrorViewModel) {
        MetricaRestClient().getCounterInfo(website.counterId, {
            WebsiteRepository.save(website, errorViewModel)
            Log.d(YANDEX_METRICA_TAG, "Счетчик успешно создан")
        }, {
            errorViewModel.setErrorMessage("Произошла ошибка добавления счетчика: $it")
        })
    }

    fun getAllWebsites(errorViewModel: ErrorViewModel) {
        MetricaRestClient().getAllCountersInfo({ counters ->
            val websites = ArrayList<Website>()
            counters.counters?.map {
                if (it.site2 == null) {
                    val website = Website(null, it.id.toLong(), it.activity_status, 0)
                    MetricaRestClient().getOneWeekPageViewsByCounterId(
                        it.id.toLong(),
                        { pageViewsResponse ->
                            if (pageViewsResponse.totals == null) {
                                Log.w(YANDEX_METRICA_TAG, "Ошибка получения посещений сайта")
                            } else {
                                website.pageViews = pageViewsResponse.totals
                            }
                        },
                        {
                            Log.e(YANDEX_METRICA_TAG, "Ошибка получения посещений сайта")
                            errorViewModel.setErrorMessage("Ошибка получения количества посещений сайта ${website.name}")
                        })
                    websites.plus(website)
                } else {
                    val website = Website(it.site2.site, it.id.toLong(), it.activity_status, 0)
                    MetricaRestClient().getOneWeekPageViewsByCounterId(
                        it.id.toLong(),
                        { pageViewsResponse ->
                            if (pageViewsResponse.totals == null) {
                                Log.w(YANDEX_METRICA_TAG, "Ошибка получения посещений сайта")
                            } else {
                                website.pageViews = pageViewsResponse.totals
                            }
                        },
                        {
                            Log.e(YANDEX_METRICA_TAG, "Ошибка получения посещений сайта")
                            errorViewModel.setErrorMessage("Ошибка получения количества посещений сайта ${website.name}")
                        })
                    websites.plus(website)
                }
            }
            WebsiteRepository.saveAll(websites)
        },
            {
                errorViewModel.setErrorMessage("Неизвестная ошибка: $it")
            })
    }

    fun getByCounterId(counterId: Long, errorViewModel: ErrorViewModel) {
        MetricaRestClient().getCounterInfo(counterId, {
            val found = WebsiteRepository.findByCounterId(counterId)
            if (found != null) {
                if (it.site2 != null) {
                    found.name = it.site2.site
                }
                found.activity = it.activity_status
                MetricaRestClient().getOneWeekPageViewsByCounterId(
                    found.counterId,
                    { pageViewsResponse ->
                        if (pageViewsResponse.totals == null) {
                            Log.w(YANDEX_METRICA_TAG, "Ошибка получения посещений сайта")
                        } else {
                            found.pageViews = pageViewsResponse.totals
                            WebsiteRepository.edit(found)
                        }
                    },
                    {
                        Log.e(YANDEX_METRICA_TAG, "Ошибка получения посещений сайта")
                        errorViewModel.setErrorMessage("Ошибка получения количества посещений сайта ${found.name}")
                    })
            }
        },
            {
                errorViewModel.setErrorMessage(it.toString())
            })
    }

}
