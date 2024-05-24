package com.example.nam.vaidation

object EmailValidator {

    fun isValid(email: String): String? {
        return if (email.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"))) {
            null
        } else {
            "Неверный формат E-mail"
        }
    }

}