package com.example.nam.storage

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context, storageName: String) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(storageName, Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

}