package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedItemsListScreen(
    state: EnhancedUiState,
    snackbarHostState: SnackbarHostState,
    onItemClick: (Item) -> Unit,
    onToggleFavorite: (Item) -> Unit,
    onAddItem: (String) -> Unit,
    onDeleteItem: (Item) -> Unit,
    onQueryChange: (String) -> Unit,
    onFilterChange: (ItemFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddSheet by remember { mutableStateOf(false) }
    var searchActive by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (searchActive) {
                SearchTopBar(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    onClose = {
                        searchActive = false
                        onQueryChange("")
                    },
                )
            } else {
                TopAppBar(
                    title = {
                        Column {
                            Text(stringResource(R.string.enh_title))
                            Text(
                                text = itemCountSummary(state.totalCount, state.favoriteCount),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { searchActive = true }) {
                            Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.enh_search))
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.enh_add_item))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FilterChips(selected = state.filter, onFilterChange = onFilterChange)

            if (state.isEmpty) {
                EnhancedEmptyState(state = state, modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.items, key = { it.id }) { item ->
                        SwipeableItemRow(
                            item = item,
                            onClick = { onItemClick(item) },
                            onToggleFavorite = { onToggleFavorite(item) },
                            onDelete = { onDeleteItem(item) },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        EnhancedAddItemSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name ->
                onAddItem(name)
                showAddSheet = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text(stringResource(R.string.enh_search)) },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        },
        actions = {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.enh_close_search))
            }
        },
    )
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChips(
    selected: ItemFilter,
    onFilterChange: (ItemFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selected == ItemFilter.ALL,
            onClick = { onFilterChange(ItemFilter.ALL) },
            label = { Text(stringResource(R.string.enh_filter_all)) },
        )
        FilterChip(
            selected = selected == ItemFilter.FAVORITES,
            onClick = { onFilterChange(ItemFilter.FAVORITES) },
            label = { Text(stringResource(R.string.enh_filter_favorites)) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableItemRow(
    item: Item,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    // Swiping to the dismissed anchor removes the item from the source list,
    // which takes the row out of composition — so there's nothing to reset.
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        },
    ) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            EnhancedItemRow(
                item = item,
                onToggleFavorite = onToggleFavorite,
                modifier = Modifier.clickable(onClick = onClick),
            )
        }
    }
}

@Composable
private fun itemCountSummary(total: Int, favorites: Int): String {
    val items = pluralStringResource(R.plurals.enh_item_count, total, total)
    val favs = pluralStringResource(R.plurals.enh_favorite_count, favorites, favorites)
    return "$items · $favs"
}

@Composable
private fun EnhancedEmptyState(
    state: EnhancedUiState,
    modifier: Modifier = Modifier,
) {
    val filtering = state.isFiltering
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = if (filtering) Icons.Outlined.SearchOff else Icons.Outlined.Inbox,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(
                if (filtering) R.string.enh_empty_search_title else R.string.enh_empty_title
            ),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            text = if (filtering && state.query.isNotBlank()) {
                stringResource(R.string.enh_empty_search_description, state.query)
            } else if (filtering) {
                stringResource(R.string.enh_filter_favorites)
            } else {
                stringResource(R.string.enh_empty_description)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnhancedItemsListScreenPreview() {
    AcademyUrielCastilloTheme {
        EnhancedItemsListScreen(
            state = EnhancedUiState(
                items = listOf(
                    Item("1", 0, "Giraffa 🦒", isFavorite = false),
                    Item("2", 1, "Leone 🦁", isFavorite = false),
                    Item("3", 2, "Lupo 🐺", isFavorite = true),
                ),
                totalCount = 3,
                favoriteCount = 1,
                isLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
            onDeleteItem = {},
            onQueryChange = {},
            onFilterChange = {},
        )
    }
}
