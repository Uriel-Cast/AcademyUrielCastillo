package com.urielcast.academytest2026.shared

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

/**
 * Landing screen that lets the reviewer choose which build to explore: the
 * [Minimal][onOpenMinimal] 1:1 SwiftUI port, the [Enhanced][onOpenEnhanced]
 * version, or the [Cupertino][onOpenCupertino] iOS skin. All three live in the
 * same APK so the project builds and runs once.
 *
 * Every card sits on the same readable surface and is differentiated by a tonal
 * icon badge, so the text stays legible in both light and dark themes. A small
 * pill at the bottom leads to the [developer profile][onOpenProfile].
 */
@Composable
fun LauncherScreen(
    onOpenMinimal: () -> Unit,
    onOpenEnhanced: () -> Unit,
    onOpenCupertino: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Item Manager",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "A SwiftUI app, rebuilt for Android. Choose a version to explore.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )

        Spacer(Modifier.size(32.dp))

        VersionCard(
            icon = Icons.Outlined.Checklist,
            title = "Minimal",
            subtitle = "A faithful 1:1 port of the original SwiftUI app. Exactly what the brief asks for.",
            onClick = onOpenMinimal,
            badgeColor = MaterialTheme.colorScheme.secondaryContainer,
            onBadgeColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )

        Spacer(Modifier.size(14.dp))

        VersionCard(
            icon = Icons.Outlined.AutoAwesome,
            title = "Enhanced",
            subtitle = "Our take, with persistence, search, undo, adaptive layouts and animations.",
            onClick = onOpenEnhanced,
            badgeColor = MaterialTheme.colorScheme.primaryContainer,
            onBadgeColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Spacer(Modifier.size(14.dp))

        VersionCard(
            icon = Icons.Outlined.PhoneIphone,
            title = "Cupertino",
            subtitle = "The same app, hand-skinned to look and feel like native iOS.",
            onClick = onOpenCupertino,
            badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
            onBadgeColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )

        Spacer(Modifier.size(28.dp))

        MeetTheDeveloperPill(
            onClick = onOpenProfile,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

/** Friendly pill inviting the reviewer to meet the person behind the project. */
@Composable
private fun MeetTheDeveloperPill(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp, end = 14.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.profile_photo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
            )
            Text(
                text = "Want to meet the person behind this?",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 10.dp),
            )
        }
    }
}

@Composable
private fun VersionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    badgeColor: Color,
    onBadgeColor: Color,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = badgeColor,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = onBadgeColor)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 780)
@Composable
private fun LauncherScreenPreview() {
    AcademyUrielCastilloTheme {
        LauncherScreen(onOpenMinimal = {}, onOpenEnhanced = {}, onOpenCupertino = {}, onOpenProfile = {})
    }
}

@Preview(showBackground = true, heightDp = 780, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LauncherScreenDarkPreview() {
    AcademyUrielCastilloTheme(darkTheme = true) {
        LauncherScreen(onOpenMinimal = {}, onOpenEnhanced = {}, onOpenCupertino = {}, onOpenProfile = {})
    }
}
