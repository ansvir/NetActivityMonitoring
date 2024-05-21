package com.example.nam.storage

import com.example.nam.MainActivity
import com.example.nam.storage.dto.Website
import com.google.gson.Gson

class WebsiteRepository {

    private var preferencesManager: PreferencesManager =
        PreferencesManager(MainActivity.self.baseContext, "WEBSITES")

    fun findById(id: Int): Website? {
        val found = preferencesManager.getDataByKey(id.toString()) ?: return null
        return fromJson(found)
    }

    fun find(): List<Website> {
        return preferencesManager.getData()
    }

    fun findByCounterId(id: Long): Website? {
        return preferencesManager.getData().find { it.counterId == id }
    }

    fun save(website: Website) {
        if (website.id == null) {
            website.id = (find().sortedBy { website.id }.last().id!!.plus(1))
        }
        preferencesManager.saveData(website.id.toString(), toJson(website))
    }

    fun editById(website: Website) {
        val found = preferencesManager.getData().find { it.id == website.id }
        if (found == null) {
            return
        }
        found.name = website.name
        found.counterId = website.counterId
        preferencesManager.saveData(found.id.toString(), toJson(found))
    }

    fun delete(website: Website) {
        preferencesManager.deleteData(website.id.toString())
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
