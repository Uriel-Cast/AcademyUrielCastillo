package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.urielcast.academytest2026.R

/** Gold used for the favorite star across the Enhanced version. */
val FavoriteGold = Color(0xFFF5C518)

/**
 * Favorite toggle for the Enhanced version. Plain for now; animation and
 * haptics are added in the UX pass.
 */
@Composable
fun EnhancedFavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(
        if (isFavorite) R.string.enh_remove_from_favorites else R.string.enh_add_to_favorites
    )

    IconButton(
        onClick = onToggle,
        modifier = modifier.semantics { contentDescription = description },
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = null,
            tint = if (isFavorite) FavoriteGold else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
