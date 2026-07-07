package com.urielcast.academytest2026.minimal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * The detail screen — the Android counterpart of SwiftUI's `ItemDetailView`.
 * Shows the item name, a favorite toggle, and a delete action in the top bar.
 * Stateless: the item is passed in and observed by the caller, so favorite
 * toggles reflect live.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalItemDetailScreen(
    item: Item,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = MinimalStrings.CANCEL,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = MinimalStrings.DELETE_ITEM,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = MinimalStrings.ITEM_SECTION,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
            )
            ListItem(
                headlineContent = { Text(MinimalStrings.NAME) },
                trailingContent = { Text(item.name) },
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text(MinimalStrings.FAVORITE) },
                trailingContent = {
                    MinimalFavoriteButton(isFavorite = item.isFavorite, onToggle = onToggleFavorite)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MinimalItemDetailScreenPreview() {
    AcademyUrielCastilloTheme {
        MinimalItemDetailScreen(
            item = Item(id = "1", creationIndex = 0, name = "Álgebra", isFavorite = false),
            onToggleFavorite = {},
            onDelete = {},
            onBack = {},
        )
    }
}
