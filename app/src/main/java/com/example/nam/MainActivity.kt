package com.example.nam

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nam.screen.MainScreen
import com.example.nam.theme.AppTheme

class MainActivity : AppCompatActivity() {

    private lateinit var _context: Context
    private var context: Context
        get() = _context
        set(value) {
            _context = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        this.context = baseContext
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}
