package com.example.nam.storage

import com.example.nam.MainActivity
import com.example.nam.storage.dto.Website
import com.google.gson.Gson

object WebsiteRepository {

    private var preferencesManager: PreferencesManager =
        PreferencesManager(MainActivity.self.baseContext, "WEBSITES")

    fun findByName(name: String): Website? {
        val found = preferencesManager.getDataByKey(name) ?: return null
        return fromJson(found)
    }

    fun find(): List<Website> {
        return preferencesManager.getData()
    }

    fun findByCounterId(id: Long): Website? {
        return preferencesManager.getData<Website>().find { it.counterId == id }
    }

    fun save(website: Website, errorViewModel: ErrorViewModel) {
        if (website.name?.let { findByName(it) } == null) {
            website.name?.let { preferencesManager.saveData(it, toJson(website)) }
        } else {
            errorViewModel.setErrorMessage("Сайт с таким именем уже добавлен!")
        }
    }

    fun saveAll(websites: List<Website>) {
        websites.forEach { website -> website.name?.let { preferencesManager.saveData(it, Gson().toJson(website)) } }
    }

    fun edit(website: Website) {
        val found = preferencesManager.getData<Website>().find { it.name == website.name }
        if (found == null) {
            return
        } else {
            preferencesManager.saveData(website.name.toString(), toJson(website))
        }
    }

    fun delete(website: Website) {
        website.name?.let { preferencesManager.deleteData(it) }
    }

    fun deleteAll() {
        preferencesManager.deleteData()
    }

    private fun fromJson(json: String): Website {
        val gson = Gson()
        return gson.fromJson(json, Website::class.java)
    }

    private fun toJson(website: Website): String {
        val gson = Gson()
        return gson.toJson(website)
    }

}
