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

    fun addWebsite(website: Website) {
        MetricaRestClient().createCounter(
            MetricaCounterDto(
                MetricaCounterRequestDto(
                    name = website.name,
                    site2 = Site(website.name)
                )
            ),
            {
                WebsiteRepository.save(website)
                Log.d(YANDEX_METRICA_TAG, "Счетчик успешно создан")
            },
            {
                CacheRepository.put(CacheRepository.CacheType.NOTIFICATION, it.toString())
            })
    }

    fun getAllWebsites() {
        MetricaRestClient().getAllCountersInfo({ counters ->
            val websites = ArrayList<Website>()
            counters.counters?.map {
                if (it.site2 == null) {
                    websites.plus(Website(null, it.id.toLong(), it.activity_status.toInt()))
                } else {
                    websites.plus(Website(it.site2.site, it.id.toLong(), it.activity_status.toInt()))
                }
            }
        },
            {
                CacheRepository.put(
                    CacheRepository.CacheType.NOTIFICATION,
                    it.toString()
                )
            })
    }

    fun getByCounterId(counterId: Long) {
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
                CacheRepository.put(
                    CacheRepository.CacheType.NOTIFICATION,
                    it.toString()
                )
            })
    }

    fun getAllSettings(): List<Setting> {
        return listOf(
            Setting(1, "Настройка 1", "Данные настройки"),
            Setting(2, "Настройка 2", "Данные настройки"),
        )
    }

}
