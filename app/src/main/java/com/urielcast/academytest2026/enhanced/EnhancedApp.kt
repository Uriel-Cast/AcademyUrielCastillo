package com.urielcast.academytest2026.enhanced

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemDetailScreen
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemsListScreen
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemsViewModel
import kotlinx.serialization.Serializable

@Serializable
private data object EnhancedListRoute : NavKey

@Serializable
private data class EnhancedDetailRoute(val id: String) : NavKey

/**
 * Entry point of the Enhanced version. Room-backed via [EnhancedItemsViewModel];
 * a Navigation 3 back stack pushes the detail over the list. The adaptive
 * list-detail layout is layered on in the adaptive pass.
 */
@Composable
fun EnhancedApp(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
) {
    val viewModel: EnhancedItemsViewModel = viewModel(factory = EnhancedItemsViewModel.Factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(EnhancedListRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = entryProvider {
            entry<EnhancedListRoute> {
                EnhancedItemsListScreen(
                    state = state,
                    onItemClick = { backStack.add(EnhancedDetailRoute(it.id)) },
                    onToggleFavorite = viewModel::toggleFavorite,
                    onAddItem = viewModel::addItem,
                )
            }

            entry<EnhancedDetailRoute> { route ->
                val item by remember(route.id) { viewModel.observeItem(route.id) }
                    .collectAsStateWithLifecycle(initialValue = null)

                // Distinguish "still loading" from "deleted": only leave the
                // detail once we've seen the item and it later disappears.
                var hasLoaded by remember(route.id) { mutableStateOf(false) }
                LaunchedEffect(item) {
                    if (item != null) hasLoaded = true
                    else if (hasLoaded) backStack.removeLastOrNull()
                }

                item?.let { loaded ->
                    EnhancedItemDetailScreen(
                        item = loaded,
                        onToggleFavorite = { viewModel.toggleFavorite(loaded) },
                        onDelete = {
                            viewModel.delete(loaded)
                            backStack.removeLastOrNull()
                        },
                        onBack = { backStack.removeLastOrNull() },
                    )
                }
            }
        },
    )

    BackHandler(enabled = backStack.size <= 1) { onExit() }
}
