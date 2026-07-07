package com.urielcast.academytest2026.enhanced.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Light/dark preference for the Enhanced version. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** UI language for the Enhanced version. `null` tag means "follow the system". */
enum class AppLanguage(val tag: String?) {
    SYSTEM(null),
    ITALIAN("it"),
    ENGLISH("en"),
    SPANISH("es"),
}

data class AppSettings(
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.SYSTEM,
)

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

/**
 * Persists the Enhanced version's user preferences (theme + language) with
 * DataStore, exposed as a reactive [Flow] so the UI recomposes when they change.
 */
class SettingsRepository(context: Context) {

    private val dataStore = context.applicationContext.settingsDataStore

    val settings: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            theme = prefs[THEME_KEY]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
                ?: ThemeMode.SYSTEM,
            language = prefs[LANGUAGE_KEY]?.let { tag -> AppLanguage.entries.find { it.name == tag } }
                ?: AppLanguage.SYSTEM,
        )
    }

    suspend fun setTheme(mode: ThemeMode) {
        dataStore.edit { it[THEME_KEY] = mode.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    private companion object {
        val THEME_KEY = stringPreferencesKey("theme_mode")
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }
}
