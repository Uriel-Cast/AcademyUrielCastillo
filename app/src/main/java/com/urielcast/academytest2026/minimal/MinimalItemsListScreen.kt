package com.urielcast.academytest2026.minimal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.core.model.DefaultItems
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.core.model.sortedForDisplay
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * The list screen — the Android counterpart of SwiftUI's `ItemsListView`.
 * Stateless: it receives already-sorted [items] and reports intents upward, so
 * it renders in previews without a ViewModel. It owns only the transient
 * "is the add sheet showing" flag.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalItemsListScreen(
    items: List<Item>,
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
                title = { Text(MinimalStrings.LIST_TITLE) },
                actions = {
                    IconButton(onClick = { showAddSheet = true }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = MinimalStrings.ADD_ITEM,
                        )
                    }
                },
            )
        },
    ) { padding ->
        if (items.isEmpty()) {
            MinimalEmptyState(
                onAdd = { showAddSheet = true },
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
                items(items, key = { it.id }) { item ->
                    MinimalItemRow(
                        item = item,
                        onToggleFavorite = { onToggleFavorite(item) },
                        modifier = Modifier
                            .clickable { onItemClick(item) }
                            .padding(horizontal = 16.dp),
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showAddSheet) {
        MinimalAddItemSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name ->
                onAddItem(name)
                showAddSheet = false
            },
        )
    }
}

@Composable
private fun MinimalEmptyState(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            text = MinimalStrings.EMPTY_TITLE,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            text = MinimalStrings.EMPTY_LIST_DESCRIPTION,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
        FilledTonalButton(onClick = onAdd, modifier = Modifier.padding(top = 16.dp)) {
            Text(MinimalStrings.ADD_ITEM)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MinimalItemsListScreenPreview() {
    AcademyUrielCastilloTheme {
        MinimalItemsListScreen(
            items = DefaultItems.list.sortedForDisplay(),
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun MinimalItemsListScreenEmptyPreview() {
    AcademyUrielCastilloTheme {
        MinimalItemsListScreen(
            items = emptyList(),
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
        )
    }
}
