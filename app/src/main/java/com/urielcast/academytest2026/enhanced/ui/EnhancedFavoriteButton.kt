package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.urielcast.academytest2026.R

/** Gold used for the favorite star across the Enhanced version. */
val FavoriteGold = Color(0xFFF5C518)

/**
 * Favorite toggle for the Enhanced version: the star colour crossfades and the
 * icon springs with a little bounce on every tap, with a haptic tick — small
 * touches that make the state change feel deliberate.
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
    val haptics = LocalHapticFeedback.current
    val scale = remember { Animatable(1f) }
    val tint by animateColorAsState(
        targetValue = if (isFavorite) FavoriteGold else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "favoriteTint",
    )

    IconButton(
        onClick = {
            onToggle()
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = modifier.semantics { contentDescription = description },
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        )
    }

    // Bounce whenever the favorite state actually changes — but not on the first
    // composition (which would fire while just scrolling rows into view).
    var isFirstComposition by remember { mutableStateOf(true) }
    LaunchedEffect(isFavorite) {
        if (isFirstComposition) {
            isFirstComposition = false
            return@LaunchedEffect
        }
        scale.snapTo(0.6f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        )
    }
}
