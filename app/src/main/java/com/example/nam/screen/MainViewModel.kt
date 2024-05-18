package com.example.nam.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nam.storage.WebsiteRepository
import com.example.nam.storage.dto.Setting
import com.example.nam.storage.dto.Website

class MainViewModel(websitesRepository: WebsiteRepository) : ViewModel() {

    private var websiteRepository = websitesRepository;

    fun editWebsite(website: Website) {
        this.websiteRepository.edit(website)
    }

    fun addWebsite(website: Website) {
        this.websiteRepository.save(website)
    }

    fun getAllSites(): List<Website> {
        return listOf(
            Website(1, "www.mail.ru", 1),
            Website(2, "www.mysite.org", 2),
            Website(3, "www.example.com", 3)
        )
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
