package com.example.nam.storage

import java.util.concurrent.ConcurrentHashMap

object CacheRepository {

    val cache = ConcurrentHashMap<CacheType, String>()

    fun get(cacheType: CacheType): String? {
        return this.cache[cacheType]
    }

    fun put(cacheType: CacheType, value: String) {
        cache[cacheType] = value
    }

    enum class CacheType {
        NOTIFICATION, WEBSITES
    }

}