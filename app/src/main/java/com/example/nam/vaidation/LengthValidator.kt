package com.example.nam.vaidation

object LengthValidator {

    fun isValid(text: String, length: Int): String? {
        return if (text.length == length) {
            null
        } else {
            "Неверная длина строки."
        }
    }

    fun isValid(text: String, lengthMin: Int, lengthMax: Int): String? {
        return if (text.length in lengthMin..lengthMax) {
            null
        } else {
            "Неверная длина строки."
        }
    }

}