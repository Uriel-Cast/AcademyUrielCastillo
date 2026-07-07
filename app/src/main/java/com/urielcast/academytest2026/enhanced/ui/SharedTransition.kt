package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

/**
 * The [SharedTransitionScope] created by the Enhanced version's
 * `SharedTransitionLayout`, exposed so deep composables (the item row, the
 * detail title) can opt into a shared-element morph without threading the scope
 * through every function signature. Null outside that layout (e.g. previews).
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/**
 * Marks a composable as the shared "name" element for [itemId], so the item name
 * morphs between the list row and the detail screen during the push/pop.
 *
 * A no-op when there's no shared-transition scope (previews) or when the list and
 * detail are shown together in the two-pane scene — where both would be on screen
 * at once and share the same key.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedItemName(itemId: String): Modifier {
    val sharedScope = LocalSharedTransitionScope.current
    if (sharedScope == null || LocalInTwoPaneScene.current) return this

    return with(sharedScope) {
        this@sharedItemName.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "item-name-$itemId"),
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        )
    }
}
