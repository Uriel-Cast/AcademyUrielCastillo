package com.urielcast.academytest2026.minimal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * A single row: name on top, favorite state as a caption, and a favorite toggle
 * on the trailing edge — the Android counterpart of the SwiftUI `ItemRowView`.
 */
@Composable
fun MinimalItemRow(
    item: Item,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = if (item.isFavorite) MinimalStrings.FAVORITE else MinimalStrings.NOT_FAVORITE,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.padding(horizontal = 6.dp))

        MinimalFavoriteButton(isFavorite = item.isFavorite, onToggle = onToggleFavorite)
    }
}

@Preview(showBackground = true)
@Composable
private fun MinimalItemRowPreview() {
    AcademyUrielCastilloTheme {
        Column {
            MinimalItemRow(
                item = Item(id = "1", creationIndex = 0, name = "Caffe", isFavorite = true),
                onToggleFavorite = {},
            )
            MinimalItemRow(
                item = Item(id = "2", creationIndex = 1, name = "Zaino", isFavorite = false),
                onToggleFavorite = {},
            )
        }
    }
}
