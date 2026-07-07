package com.urielcast.academytest2026.enhanced

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Entry point of the Enhanced version.
 *
 * Placeholder for now — the persistence layer, adaptive list-detail, undo,
 * animations and i18n land in the following commits.
 */
@Composable
fun EnhancedApp(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
) {
    BackHandler { onExit() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Enhanced version — under construction",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}
