package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * Detail screen for the Enhanced version: name (editable via a rename dialog),
 * favorite toggle and delete. [showBackButton] is false when shown side-by-side
 * with the list on a wide screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedItemDetailScreen(
    item: Item,
    onToggleFavorite: () -> Unit,
    onRename: (String) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
) {
    var editing by remember { mutableStateOf(false) }

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
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.name)
                        IconButton(onClick = { editing = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = stringResource(R.string.enh_edit_name),
                            )
                        }
                    }
                },
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

    if (editing) {
        RenameDialog(
            initialName = item.name,
            onConfirm = {
                onRename(it)
                editing = false
            },
            onDismiss = { editing = false },
        )
    }
}

@Composable
private fun RenameDialog(
    initialName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.enh_edit_name)) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.enh_name)) },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim()) },
                enabled = name.isNotBlank(),
            ) { Text(stringResource(R.string.enh_save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.enh_cancel)) }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun EnhancedItemDetailScreenPreview() {
    AcademyUrielCastilloTheme {
        EnhancedItemDetailScreen(
            item = Item("1", 0, "Lupo 🐺", isFavorite = true),
            onToggleFavorite = {},
            onRename = {},
            onDelete = {},
            onBack = {},
        )
    }
}
