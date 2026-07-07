package com.urielcast.academytest2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.urielcast.academytest2026.minimal.MinimalApp
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AcademyUrielCastilloTheme {
                MinimalApp()
            }
        }
    }
}
