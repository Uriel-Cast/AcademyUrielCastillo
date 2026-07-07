package com.urielcast.academytest2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.urielcast.academytest2026.enhanced.settings.AppSettings
import com.urielcast.academytest2026.enhanced.settings.ProvideAppLocale
import com.urielcast.academytest2026.enhanced.settings.ThemeMode
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository = (application as AcademyApplication).container.settingsRepository
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(AppSettings())

            val darkTheme = when (settings.theme) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            ProvideAppLocale(settings.language) {
                AcademyUrielCastilloTheme(darkTheme = darkTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        RootApp()
                    }
                }
            }
        }
    }
}
