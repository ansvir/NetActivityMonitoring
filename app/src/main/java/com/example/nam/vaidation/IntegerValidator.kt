package com.example.nam.vaidation

object IntegerValidator {

    fun isValid(text: String): String? {
        return if (text.toIntOrNull() != null) {
            null
        } else {
            "Неверный числовой формат"
        }
    }

}