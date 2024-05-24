package com.example.nam.storage

import android.util.Log
import com.example.nam.service.MetricaRestClient
import com.example.nam.service.MetricaRestClient.Companion.YANDEX_METRICA_TAG
import com.example.nam.storage.dto.MetricaCounterDto
import com.example.nam.storage.dto.MetricaCounterRequestDto
import com.example.nam.storage.dto.Setting
import com.example.nam.storage.dto.Site
import com.example.nam.storage.dto.Website

object WebsiteService {

    fun editWebsite(website: Website) {
        // todo
    }

    fun addWebsite(website: Website, errorViewModel: ErrorViewModel) {
        MetricaRestClient().createCounter(
            MetricaCounterDto(
                MetricaCounterRequestDto(
                    name = website.name,
                    site2 = Site(website.name)
                )
            ),
            {
                WebsiteRepository.save(website, errorViewModel)
                Log.d(YANDEX_METRICA_TAG, "Счетчик успешно создан")
            },
            {
                errorViewModel.setErrorMessage(it.toString())
            })
    }

    fun getAllWebsites(errorViewModel: ErrorViewModel) {
        MetricaRestClient().getAllCountersInfo({ counters ->
            val websites = ArrayList<Website>()
            counters.counters?.map {
                if (it.site2 == null) {
                    websites.plus(Website(null, it.id.toLong(), it.activity_status.toInt()))
                } else {
                    websites.plus(Website(it.site2.site, it.id.toLong(), it.activity_status.toInt()))
                }
            }
            WebsiteRepository.saveAll(websites)
        },
            {
                errorViewModel.setErrorMessage(it.toString())
            })
    }

    fun getByCounterId(counterId: Long, errorViewModel: ErrorViewModel) {
        MetricaRestClient().getCounterInfo(counterId, {
            val found = WebsiteRepository.findByCounterId(counterId)
            if (found != null) {
                if (it.site2 != null) {
                    found.name = it.site2.site
                }
                found.activity = it.activity_status.toInt()
                WebsiteRepository.edit(found)
            }
        },
            {
                errorViewModel.setErrorMessage(it.toString())
            })
    }

}
