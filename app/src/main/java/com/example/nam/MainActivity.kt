package com.example.nam

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nam.screen.MainScreen
import com.example.nam.theme.AppTheme

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var self: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        self = this
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}
