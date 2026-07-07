package com.urielcast.academytest2026.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * Landing screen that lets the reviewer choose which build to explore:
 * the [Minimal][onOpenMinimal] 1:1 SwiftUI port, or the [Enhanced][onOpenEnhanced]
 * version. Both live in the same APK so it builds and runs once.
 */
@Composable
fun LauncherScreen(
    onOpenMinimal: () -> Unit,
    onOpenEnhanced: () -> Unit,
    onOpenCupertino: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Item Manager",
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = "SwiftUI → Jetpack Compose. Pick a version to explore.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )

        Spacer(Modifier.size(32.dp))

        VersionCard(
            icon = Icons.Outlined.Checklist,
            title = "Minimal",
            subtitle = "Faithful 1:1 port of the original SwiftUI app. Exactly what the brief asks for.",
            onClick = onOpenMinimal,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )

        Spacer(Modifier.size(16.dp))

        VersionCard(
            icon = Icons.Outlined.AutoAwesome,
            title = "Enhanced",
            subtitle = "Our take: persistence, undo, search, adaptive panes, animations and i18n.",
            onClick = onOpenEnhanced,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )

        Spacer(Modifier.size(16.dp))

        VersionCard(
            icon = Icons.Outlined.PhoneIphone,
            title = "Cupertino",
            subtitle = "How close can Compose get to iOS? A hand-built Cupertino skin of the same app.",
            onClick = onOpenCupertino,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    }
}

@Composable
private fun VersionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    containerColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 720)
@Composable
private fun LauncherScreenPreview() {
    AcademyUrielCastilloTheme {
        LauncherScreen(onOpenMinimal = {}, onOpenEnhanced = {}, onOpenCupertino = {})
    }
}
