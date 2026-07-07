package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

@Composable
fun EnhancedItemRow(
    item: Item,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.padding(start = 4.dp).weight(1f)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(
                    if (item.isFavorite) R.string.enh_favorite else R.string.enh_not_favorite
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        EnhancedFavoriteButton(isFavorite = item.isFavorite, onToggle = onToggleFavorite)
    }
}

@Preview(showBackground = true)
@Composable
private fun EnhancedItemRowPreview() {
    AcademyUrielCastilloTheme {
        Column {
            EnhancedItemRow(
                item = Item("1", 0, "Lupo 🐺", isFavorite = true),
                onToggleFavorite = {},
            )
            EnhancedItemRow(
                item = Item("2", 1, "Giraffa 🦒", isFavorite = false),
                onToggleFavorite = {},
            )
        }
    }
}
