package com.example.nam.storage

import com.example.nam.MainActivity
import com.example.nam.storage.dto.Website
import com.example.nam.storage.dto.YandexToken
import com.google.gson.Gson

object TokenRepository {

    private var preferencesManager: PreferencesManager =
        PreferencesManager(MainActivity.self.baseContext, "YANDEX_TOKEN")

    private const val TOKEN_KEY = "TOKEN_KEY"

    fun find(): YandexToken? {
        return preferencesManager.getDataByKey(TOKEN_KEY)?.let { fromJson(it) }
    }

    fun save(token: YandexToken) {
        preferencesManager.saveData(TOKEN_KEY, toJson(token))
    }

    fun deleteAll() {
        preferencesManager.deleteData()
    }

    private fun fromJson(json: String): YandexToken {
        val gson = Gson()
        return gson.fromJson(json, YandexToken::class.java)
    }

    private fun toJson(yandexToken: YandexToken): String {
        val gson = Gson()
        return gson.toJson(yandexToken)
    }

}
