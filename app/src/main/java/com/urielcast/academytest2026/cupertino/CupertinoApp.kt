package com.urielcast.academytest2026.cupertino

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
private data object CupertinoListRoute : NavKey

@Serializable
private data class CupertinoDetailRoute(val id: String) : NavKey

/**
 * Entry point of the Cupertino version: a Navigation 3 stack styled like iOS —
 * pushes slide in from the right while the previous screen parallaxes out to the
 * left, and the interactive predictive-back pop mirrors it.
 */
@Composable
fun CupertinoApp(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    viewModel: CupertinoItemsViewModel = viewModel(),
) {
    CupertinoTheme {
        val colors = LocalCupertinoColors.current
        val backStack = rememberNavBackStack(CupertinoListRoute)
        val items by viewModel.items.collectAsStateWithLifecycle()

        Box(
            modifier
                .fillMaxSize()
                .background(colors.systemBackground),
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                transitionSpec = {
                    slideInHorizontally(tween(350)) { it } togetherWith
                        slideOutHorizontally(tween(350)) { -it / 3 }
                },
                popTransitionSpec = {
                    slideInHorizontally(tween(350)) { -it / 3 } togetherWith
                        slideOutHorizontally(tween(350)) { it }
                },
                predictivePopTransitionSpec = {
                    slideInHorizontally(tween(350)) { -it / 3 } togetherWith
                        slideOutHorizontally(tween(350)) { it }
                },
                entryProvider = entryProvider {
                    entry<CupertinoListRoute> {
                        CupertinoListScreen(
                            items = items,
                            onItemClick = { backStack.add(CupertinoDetailRoute(it.id)) },
                            onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                            onAddItem = { viewModel.addItem(it) },
                            onDelete = { viewModel.delete(it.id) },
                        )
                    }

                    entry<CupertinoDetailRoute> { route ->
                        val item = items.firstOrNull { it.id == route.id }

                        LaunchedEffect(item == null) {
                            if (item == null) backStack.removeLastOrNull()
                        }

                        if (item != null) {
                            CupertinoDetailScreen(
                                item = item,
                                onToggleFavorite = { viewModel.toggleFavorite(item.id) },
                                onDelete = { viewModel.delete(item.id) },
                                onBack = { backStack.removeLastOrNull() },
                            )
                        }
                    }
                },
            )
        }

        BackHandler(enabled = backStack.size <= 1) { onExit() }
    }
}
