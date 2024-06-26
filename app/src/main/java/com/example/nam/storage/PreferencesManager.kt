package com.example.nam.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.nam.storage.dto.Website
import com.google.gson.Gson

class PreferencesManager(context: Context, storageName: String) {

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(storageName, Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    inline fun<reified T> getData(): List<T> {
        return sharedPreferences.all.values
            .map { Gson().fromJson(it.toString(), T::class.java) }
            .toCollection(ArrayList())
    }

    fun getDataByKey(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun deleteData(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun deleteData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}