package com.urielcast.academytest2026.minimal

import androidx.activity.compose.BackHandler
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
private data object MinimalListRoute : NavKey

@Serializable
private data class MinimalDetailRoute(val id: String) : NavKey

/**
 * Entry point of the Minimal version: a Navigation 3 back stack that pushes the
 * detail screen on top of the list, mirroring how SwiftUI's `NavigationSplitView`
 * collapses to a stack on a phone. One [MinimalItemsViewModel] is shared by both
 * destinations so edits are reflected everywhere.
 */
@Composable
fun MinimalApp(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    viewModel: MinimalItemsViewModel = viewModel(),
) {
    val backStack = rememberNavBackStack(MinimalListRoute)
    val items by viewModel.items.collectAsStateWithLifecycle()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = entryProvider {
            entry<MinimalListRoute> {
                MinimalItemsListScreen(
                    items = items,
                    onItemClick = { backStack.add(MinimalDetailRoute(it.id)) },
                    onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                    onAddItem = { viewModel.addItem(it) },
                )
            }

            entry<MinimalDetailRoute> { route ->
                val item = items.firstOrNull { it.id == route.id }

                // If the shown item disappears (deleted here or from the list),
                // leave the detail screen — the SwiftUI app clears its selection
                // the same way in deleteFromDetail.
                LaunchedEffect(item == null) {
                    if (item == null) backStack.removeLastOrNull()
                }

                if (item != null) {
                    MinimalItemDetailScreen(
                        item = item,
                        onToggleFavorite = { viewModel.toggleFavorite(item.id) },
                        onDelete = { viewModel.delete(item.id) },
                        onBack = { backStack.removeLastOrNull() },
                    )
                }
            }
        },
    )

    // At the root of this version, Back returns to the launcher rather than
    // popping an empty stack. Composed after NavDisplay so it takes priority
    // only when it is enabled (i.e. nothing left for NavDisplay to pop).
    BackHandler(enabled = backStack.size <= 1) { onExit() }
}
