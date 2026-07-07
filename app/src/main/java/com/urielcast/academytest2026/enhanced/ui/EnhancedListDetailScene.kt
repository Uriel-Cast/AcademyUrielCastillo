package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.contains
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

/**
 * A Navigation 3 [Scene] that shows the list and detail side-by-side in a 40/60
 * split — the tablet/foldable form of the SwiftUI `NavigationSplitView`.
 *
 * Adapted from the official Nav3 "scenes-listdetail" recipe. We roll our own
 * (rather than use `adaptive-navigation3`'s `ListDetailSceneStrategy`) because
 * that artifact only exists in the 1.3.0 line, which forces the unreleased
 * compileSdk 37 — see the README.
 */
data class EnhancedListDetailScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val listEntry: NavEntry<T>,
    val detailEntry: NavEntry<T>,
) : Scene<T> {

    override val entries: List<NavEntry<T>> = listOf(listEntry, detailEntry)

    override val content: @Composable (() -> Unit) = {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(0.4f)) {
                listEntry.Content()
            }

            VerticalDivider()

            // The detail is shown in-pane, so it must not draw its own back button.
            CompositionLocalProvider(LocalInTwoPaneScene provides true) {
                Column(modifier = Modifier.weight(0.6f)) {
                    AnimatedContent(
                        targetState = detailEntry,
                        contentKey = { it.contentKey },
                        transitionSpec = {
                            slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                        },
                        label = "detailPane",
                    ) { entry ->
                        entry.Content()
                    }
                }
            }
        }
    }

    companion object {
        fun listPane() = metadata { put(ListKey, true) }
        fun detailPane() = metadata { put(DetailKey, true) }
    }

    object ListKey : NavMetadataKey<Boolean>
    object DetailKey : NavMetadataKey<Boolean>
}

/** True when a detail screen is being displayed inside a two-pane scene. */
val LocalInTwoPaneScene = compositionLocalOf { false }

@Composable
fun <T : Any> rememberEnhancedListDetailSceneStrategy(): EnhancedListDetailSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return remember(windowSizeClass) { EnhancedListDetailSceneStrategy(windowSizeClass) }
}

/**
 * Returns an [EnhancedListDetailScene] when the window is at least medium-width
 * and the back stack has a list entry with a detail entry on top; otherwise null,
 * so [androidx.navigation3.ui.NavDisplay] falls back to single-pane navigation.
 */
class EnhancedListDetailSceneStrategy<T : Any>(
    private val windowSizeClass: WindowSizeClass,
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return null
        }

        val detailEntry = entries.lastOrNull()
            ?.takeIf { it.metadata.contains(EnhancedListDetailScene.DetailKey) }
            ?: return null
        val listEntry = entries.findLast { it.metadata.contains(EnhancedListDetailScene.ListKey) }
            ?: return null

        return EnhancedListDetailScene(
            key = listEntry.contentKey,
            previousEntries = entries.dropLast(1),
            listEntry = listEntry,
            detailEntry = detailEntry,
        )
    }
}
