package com.urielcast.academytest2026.minimal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * A star that toggles the favorite state — the Android counterpart of the
 * SwiftUI `FavoriteButton` (`star.fill` / `star`, yellow when favorite).
 */
@Composable
fun MinimalFavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = if (isFavorite) {
        MinimalStrings.REMOVE_FROM_FAVORITES
    } else {
        MinimalStrings.ADD_TO_FAVORITES
    }

    IconButton(
        onClick = onToggle,
        modifier = modifier.semantics { contentDescription = description },
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = null,
            tint = if (isFavorite) FavoriteYellow else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val FavoriteYellow = Color(0xFFF5C518)

@Preview(showBackground = true)
@Composable
private fun MinimalFavoriteButtonPreview() {
    AcademyUrielCastilloTheme {
        var isFavorite by remember { mutableStateOf(true) }
        MinimalFavoriteButton(isFavorite = isFavorite, onToggle = { isFavorite = !isFavorite })
    }
}
