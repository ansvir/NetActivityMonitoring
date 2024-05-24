package com.example.nam.vaidation

object DomainValidator {

    fun isValid(domain: String): String? {
        return if (domain.matches(Regex("^[a-zA-Z0-9]+\\.[a-zA-Z0-9-]+\\.[a-zA-Z0-9]+$"))) {
            null
        } else {
            "Неверный формат домена"
        }
    }

}