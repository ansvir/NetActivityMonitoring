package com.example.nam.storage.dto

data class Website(var name: String?, var counterId: Long, var activity: String?, var pageViews: Long) {
    companion object {
        fun createEmpty(): Website {
            return Website("", -1, "low", 0)
        }
    }
}