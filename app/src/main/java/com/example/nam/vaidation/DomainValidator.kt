package com.example.nam.vaidation

object DomainValidator {

    fun isValid(domain: String): String? {
        return if (domain.matches(Regex("^(([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,})\$"))) {
            null
        } else {
            "Неверный формат домена"
        }
    }

}