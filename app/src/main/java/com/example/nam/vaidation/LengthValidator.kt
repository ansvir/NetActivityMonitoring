package com.example.nam.vaidation

object LengthValidator {

    fun isValid(text: String, length: Int): String? {
        return if (text.length == length) {
            null
        } else {
            "Неверная длина строки."
        }
    }

}