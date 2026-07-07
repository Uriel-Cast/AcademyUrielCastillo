package com.urielcast.academytest2026.shared

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme

private const val LINKEDIN_URL = "https://www.linkedin.com/in/uriel-castillo-g/"
private const val GITHUB_URL = "https://github.com/Uriel-Cast"
private const val EMAIL_URI = "mailto:uriel.cast.g@gmail.com"
private const val WHATSAPP_URL = "https://wa.me/525613949426"

private val LinkedInBlue = Color(0xFF0A66C2)
private val GitHubDark = Color(0xFF24292F)
private val WhatsAppGreen = Color(0xFF25D366)

/**
 * "About the developer" screen, reached from the launcher. A hero with the
 * profile photo, followed by link-preview style cards for each public profile
 * and contact channel. Every card opens its real destination.
 */
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { onBack() }
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        // --- Hero ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(148.dp)
                    .background(
                        brush = Brush.sweepGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.primary,
                            ),
                        ),
                        shape = CircleShape,
                    )
                    .padding(5.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .padding(4.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_photo),
                    contentDescription = "Uriel Castillo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            }

            Text(
                text = "Uriel Castillo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                text = "Mobile developer",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                TechPill("Kotlin")
                TechPill("Compose")
                TechPill("SwiftUI")
            }

            Text(
                text = "The person behind this project. Thanks for taking a look, and feel free to reach out!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp),
            )
        }

        SectionHeader("Profiles")

        ProfileLinkCard(
            badge = {
                Text(
                    text = "in",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            },
            badgeColor = LinkedInBlue,
            title = "Uriel Castillo",
            subtitle = "linkedin.com/in/uriel-castillo-g",
            detail = "Experience, education and background",
            onClick = { uriHandler.openUri(LINKEDIN_URL) },
        )

        Spacer(Modifier.height(12.dp))

        ProfileLinkCard(
            badge = {
                Icon(
                    imageVector = Icons.Outlined.Code,
                    contentDescription = null,
                    tint = Color.White,
                )
            },
            badgeColor = GitHubDark,
            title = "@Uriel-Cast",
            subtitle = "github.com/Uriel-Cast",
            detail = "9 public repos, building since 2019",
            onClick = { uriHandler.openUri(GITHUB_URL) },
        )

        SectionHeader("Get in touch")

        ProfileLinkCard(
            badge = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            },
            badgeColor = MaterialTheme.colorScheme.primary,
            title = "Email",
            subtitle = "uriel.cast.g@gmail.com",
            onClick = { uriHandler.openUri(EMAIL_URI) },
        )

        Spacer(Modifier.height(12.dp))

        ProfileLinkCard(
            badge = {
                Icon(
                    imageVector = Icons.Filled.Whatsapp,
                    contentDescription = null,
                    tint = Color.White,
                )
            },
            badgeColor = WhatsAppGreen,
            title = "WhatsApp",
            subtitle = "+52 56 1394 9426",
            onClick = { uriHandler.openUri(WHATSAPP_URL) },
        )

        Text(
            text = "Built with Jetpack Compose",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
        )
    }
}

@Composable
private fun TechPill(text: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 28.dp, bottom = 10.dp, start = 4.dp),
    )
}

/**
 * A link-preview style card: a brand-colored badge, the profile's name and
 * address, an optional detail line, and an open-in-new affordance.
 */
@Composable
private fun ProfileLinkCard(
    badge: @Composable () -> Unit,
    badgeColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    detail: String? = null,
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = badgeColor,
                modifier = Modifier.size(46.dp),
            ) {
                Box(contentAlignment = Alignment.Center) { badge() }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (detail != null) {
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun ProfileScreenPreview() {
    AcademyUrielCastilloTheme {
        ProfileScreen(onBack = {})
    }
}

@Preview(showBackground = true, heightDp = 900, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenDarkPreview() {
    AcademyUrielCastilloTheme(darkTheme = true) {
        ProfileScreen(onBack = {})
    }
}
