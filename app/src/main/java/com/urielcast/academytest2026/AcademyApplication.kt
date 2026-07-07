package com.urielcast.academytest2026

import android.app.Application
import com.urielcast.academytest2026.enhanced.di.AppContainer
import com.urielcast.academytest2026.enhanced.di.DefaultAppContainer

/**
 * Application that owns the Enhanced version's [AppContainer] (database +
 * repositories). Registered via `android:name` in the manifest.
 */
class AcademyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}
