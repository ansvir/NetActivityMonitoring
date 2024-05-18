package com.example.nam.storage.dto

data class MetricaTokenDto(val accessToken: String, val expiresIn: Int) {
    companion object {
        const val CLINET_ID = "e2ac4daddb18440292dc7d99a83e907e"
        const val TOKEN_REQEUST_URL =
            "https://oauth.yandex.ru/authorize?response_type=token&client_id=$CLINET_ID"
    }
}
