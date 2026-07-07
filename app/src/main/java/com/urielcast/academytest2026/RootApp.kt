package com.urielcast.academytest2026

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.urielcast.academytest2026.enhanced.EnhancedApp
import com.urielcast.academytest2026.minimal.MinimalApp
import com.urielcast.academytest2026.shared.LauncherScreen

private enum class AppSection { Launcher, Minimal, Enhanced }

/**
 * Top-level shell. Shows the launcher, then hands the whole screen to whichever
 * version the user picks. Each version owns its own navigation; backing out of a
 * version's root returns here (via `onExit`).
 */
@Composable
fun RootApp(modifier: Modifier = Modifier) {
    var section by rememberSaveable { mutableStateOf(AppSection.Launcher) }

    when (section) {
        AppSection.Launcher -> LauncherScreen(
            onOpenMinimal = { section = AppSection.Minimal },
            onOpenEnhanced = { section = AppSection.Enhanced },
            modifier = modifier,
        )

        AppSection.Minimal -> MinimalApp(
            modifier = modifier,
            onExit = { section = AppSection.Launcher },
        )

        AppSection.Enhanced -> EnhancedApp(
            modifier = modifier,
            onExit = { section = AppSection.Launcher },
        )
    }
}
