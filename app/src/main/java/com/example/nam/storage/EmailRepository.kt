package com.example.nam.storage

import com.example.nam.MainActivity
import com.example.nam.storage.dto.Setting
import com.example.nam.storage.dto.Website
import com.google.gson.Gson

object EmailRepository {

    private var preferencesManager: PreferencesManager =
        PreferencesManager(MainActivity.self.baseContext, "EMAILS")

    fun findById(id: Int): Setting? {
        val found = preferencesManager.getDataByKey(id.toString()) ?: return null
        return fromJson(found)
    }

    fun find(): List<Setting> {
        return preferencesManager.getData<Setting>()
    }

    fun save(setting: Setting) {
        if (setting.id == null) {
            val maxId = find().maxBy { it.id!! }
            setting.id = maxId.id?.plus(1)
        } else {
            if (findById(setting.id!!) == null) {
                preferencesManager.saveData(setting.id.toString(), toJson(setting))
            } else {
                CacheRepository.put(
                    CacheRepository.CacheType.NOTIFICATION,
                    "Настройка с таким id уже добавлена!"
                )
            }
        }
    }

    fun edit(setting: Setting) {
        val found = preferencesManager.getData<Setting>().find { it.id == setting.id }
        if (found == null) {
            return
        } else {
            preferencesManager.saveData(setting.id.toString(), toJson(setting))
        }
    }

    fun delete(setting: Setting) {
        preferencesManager.deleteData(setting.id.toString())
    }

    fun deleteAll() {
        preferencesManager.deleteData()
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
