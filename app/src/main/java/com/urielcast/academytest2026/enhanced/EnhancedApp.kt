package com.urielcast.academytest2026.enhanced

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemDetailScreen
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemsListScreen
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemsViewModel
import com.urielcast.academytest2026.enhanced.ui.EnhancedListDetailScene
import com.urielcast.academytest2026.enhanced.ui.LocalInTwoPaneScene
import com.urielcast.academytest2026.enhanced.ui.LocalSharedTransitionScope
import com.urielcast.academytest2026.enhanced.ui.rememberEnhancedListDetailSceneStrategy
import com.urielcast.academytest2026.enhanced.settings.SettingsSheet
import com.urielcast.academytest2026.enhanced.settings.SettingsViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private data object EnhancedListRoute : NavKey

@Serializable
private data class EnhancedDetailRoute(val id: String) : NavKey

/**
 * Entry point of the Enhanced version. Room-backed via [EnhancedItemsViewModel];
 * a Navigation 3 back stack pushes the detail over the list. Deletes (from a
 * swipe or the detail screen) go through a shared Undo snackbar. The adaptive
 * list-detail layout is layered on in the adaptive pass.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EnhancedApp(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    viewModel: EnhancedItemsViewModel = viewModel(factory = EnhancedItemsViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(EnhancedListRoute)
    val listDetailStrategy = rememberEnhancedListDetailSceneStrategy<NavKey>()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showSettings by remember { mutableStateOf(false) }

    fun deleteWithUndo(item: Item) {
        viewModel.delete(item)
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.enh_item_deleted, item.name),
                actionLabel = context.getString(R.string.enh_undo),
                // Long, so there's a comfortable window to undo the deletion.
                duration = SnackbarDuration.Long,
            )
            if (result == SnackbarResult.ActionPerformed) viewModel.restore(item)
        }
    }

    SharedTransitionLayout(modifier = modifier) {
      val sharedScope = this
      CompositionLocalProvider(LocalSharedTransitionScope provides sharedScope) {
        NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sharedTransitionScope = sharedScope,
        sceneStrategies = listOf(listDetailStrategy),
        transitionSpec = {
            slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it / 2 }) + fadeIn() togetherWith
                fadeOut(tween(300))
        },
        popTransitionSpec = {
            fadeIn(tween(300)) togetherWith
                slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it / 2 }) + fadeOut()
        },
        predictivePopTransitionSpec = {
            fadeIn(tween(300)) togetherWith
                slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it / 2 }) + fadeOut()
        },
        entryProvider = entryProvider {
            entry<EnhancedListRoute>(metadata = EnhancedListDetailScene.listPane()) {
                EnhancedItemsListScreen(
                    state = state,
                    snackbarHostState = snackbarHostState,
                    onItemClick = { backStack.add(EnhancedDetailRoute(it.id)) },
                    onToggleFavorite = viewModel::toggleFavorite,
                    onAddItem = viewModel::addItem,
                    onDeleteItem = { deleteWithUndo(it) },
                    onQueryChange = viewModel::setQuery,
                    onFilterChange = viewModel::setFilter,
                    onOpenSettings = { showSettings = true },
                )
            }

            entry<EnhancedDetailRoute>(metadata = EnhancedListDetailScene.detailPane()) { route ->
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
                        onRename = { viewModel.rename(loaded.id, it) },
                        onDelete = {
                            deleteWithUndo(loaded)
                            backStack.removeLastOrNull()
                        },
                        onBack = { backStack.removeLastOrNull() },
                        // No back arrow when shown next to the list on a wide screen.
                        showBackButton = !LocalInTwoPaneScene.current,
                    )
                }
            }
        },
        )
      }
    }

    BackHandler(enabled = backStack.size <= 1) { onExit() }

    if (showSettings) {
        val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
        val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
        SettingsSheet(
            settings = settings,
            onThemeChange = settingsViewModel::setTheme,
            onLanguageChange = settingsViewModel::setLanguage,
            onDismiss = { showSettings = false },
        )
    }
}
