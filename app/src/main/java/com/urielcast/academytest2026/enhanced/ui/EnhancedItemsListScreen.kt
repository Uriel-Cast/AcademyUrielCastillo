package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedItemsListScreen(
    state: EnhancedUiState,
    onItemClick: (Item) -> Unit,
    onToggleFavorite: (Item) -> Unit,
    onAddItem: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.enh_add_item))
            }
        },
    ) { padding ->
        if (state.isEmpty) {
            EnhancedEmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                items(state.items, key = { it.id }) { item ->
                    EnhancedItemRow(
                        item = item,
                        onToggleFavorite = { onToggleFavorite(item) },
                        modifier = Modifier.clickable { onItemClick(item) },
                    )
                    HorizontalDivider()
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

@Composable
private fun itemCountSummary(total: Int, favorites: Int): String {
    val items = pluralStringResource(R.plurals.enh_item_count, total, total)
    val favs = pluralStringResource(R.plurals.enh_favorite_count, favorites, favorites)
    return "$items · $favs"
}

@Composable
private fun EnhancedEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Inbox,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.enh_empty_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            text = stringResource(R.string.enh_empty_description),
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
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
        )
    }
}
