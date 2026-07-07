package com.urielcast.academytest2026.enhanced.di

import android.content.Context
import androidx.room.Room
import com.urielcast.academytest2026.enhanced.data.AppDatabase
import com.urielcast.academytest2026.enhanced.data.ItemsRepository
import com.urielcast.academytest2026.enhanced.data.RoomItemsRepository
import com.urielcast.academytest2026.enhanced.settings.SettingsRepository

/**
 * Tiny manual dependency container for the Enhanced version.
 *
 * A take-home this size doesn't warrant Hilt, but the seams are the same: a
 * single place that owns the database and repositories, injected via the
 * [AcademyApplication]. Swapping in Hilt later would only mean annotating these
 * providers — the call sites wouldn't change. (See the README.)
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()
    }

    override val itemsRepository: ItemsRepository by lazy {
        RoomItemsRepository(database.itemDao())
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(context)
    }
}
