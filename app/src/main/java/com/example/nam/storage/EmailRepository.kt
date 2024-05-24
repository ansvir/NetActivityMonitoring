package com.example.nam.storage

import com.example.nam.MainActivity
import com.example.nam.storage.dto.Setting
import com.example.nam.storage.dto.Website
import com.google.gson.Gson

object EmailRepository {

    private var preferencesManager: PreferencesManager =
        PreferencesManager(MainActivity.self.baseContext, "EMAIL")

    private const val REPORT_KEY = "REPORT"

    fun find(): Setting? {
        return preferencesManager.getData<Setting>().firstOrNull()
    }

    fun save(setting: Setting) {
        preferencesManager.saveData(REPORT_KEY, toJson(setting))
    }

    private fun fromJson(json: String): Setting {
        val gson = Gson()
        return gson.fromJson(json, Setting::class.java)
    }

    private fun toJson(setting: Setting): String {
        val gson = Gson()
        return gson.toJson(setting)
    }

}
