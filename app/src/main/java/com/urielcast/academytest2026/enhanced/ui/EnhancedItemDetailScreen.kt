package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * Detail screen for the Enhanced version. Shows the name, a favorite toggle and
 * a delete action. [showBackButton] is false when it is displayed side-by-side
 * with the list on a wide screen (see the adaptive pass).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedItemDetailScreen(
    item: Item,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.enh_back),
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.enh_delete),
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
                .padding(padding),
        ) {
            Text(
                text = stringResource(R.string.enh_item_section),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.enh_name)) },
                trailingContent = { Text(item.name) },
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text(stringResource(R.string.enh_favorite)) },
                trailingContent = {
                    EnhancedFavoriteButton(isFavorite = item.isFavorite, onToggle = onToggleFavorite)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnhancedItemDetailScreenPreview() {
    AcademyUrielCastilloTheme {
        EnhancedItemDetailScreen(
            item = Item("1", 0, "Lupo 🐺", isFavorite = true),
            onToggleFavorite = {},
            onDelete = {},
            onBack = {},
        )
    }
}
