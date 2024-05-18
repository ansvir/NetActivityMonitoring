package com.example.nam.storage

import com.example.nam.storage.dto.Website

class WebsiteRepository {

    private var _websites: List<Website> = ArrayList()

    val websites: List<Website>
        get() = _websites

    fun save(website: Website) {
        _websites += website
    }

    fun edit(website: Website) {
        val found = _websites.find { it.id == website.id }
        found?.name = website.name
        found?.counterId = website.counterId
    }

    fun delete(website: Website) {
        _websites -= website
    }

}
