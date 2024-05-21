package com.example.nam.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nam.service.MetricaRestClient
import com.example.nam.service.MetricaRestClient.Companion.YANDEX_METRICA_TAG
import com.example.nam.storage.CacheRepository
import com.example.nam.storage.WebsiteRepository
import com.example.nam.storage.dto.MetricaCounterDto
import com.example.nam.storage.dto.MetricaCounterRequestDto
import com.example.nam.storage.dto.Setting
import com.example.nam.storage.dto.Site
import com.example.nam.storage.dto.Website
import com.google.gson.Gson

class MainViewModel(websitesRepository: WebsiteRepository) : ViewModel() {

    private var websiteRepository = websitesRepository

    fun editWebsite(website: Website) {
        this.websiteRepository.editById(website)
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
                this.websiteRepository.save(website)
                Log.d(YANDEX_METRICA_TAG, "Счетчик успешно создан")
            },
            {
                CacheRepository.put(CacheRepository.CacheType.NOTIFICATION, it.toString())
            })
    }

    fun getAllWebsites() {
        MetricaRestClient().getAllCountersInfo({ counters ->
            val websites = ArrayList<Website>()
            counters.map {
                websites.plus(websiteRepository.findByCounterId(it.id.toLong()))
            }
            CacheRepository.put(CacheRepository.CacheType.WEBSITES, Gson().toJson(websites))
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

    class MainViewModelFactory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(WebsiteRepository()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
