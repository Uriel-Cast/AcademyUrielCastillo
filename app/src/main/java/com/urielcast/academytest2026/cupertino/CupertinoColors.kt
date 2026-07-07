package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Apple's iOS system colors (light + dark), hand-picked so the Cupertino
 * version looks like a native iOS app rather than Material. Deliberately not
 * derived from the Material color scheme.
 */
data class CupertinoColors(
    val systemBackground: Color,
    val secondaryBackground: Color,
    val separator: Color,
    val label: Color,
    val secondaryLabel: Color,
    val tertiaryLabel: Color,
    val blue: Color,
    val red: Color,
    val yellow: Color,
    val cellHighlight: Color,
    /** Fill for the circular "liquid glass" toolbar button (iOS 26 style). */
    val glassButton: Color,
    val isDark: Boolean,
)

val LightCupertinoColors = CupertinoColors(
    systemBackground = Color(0xFFF2F2F7),
    secondaryBackground = Color(0xFFFFFFFF),
    separator = Color(0xFFC6C6C8),
    label = Color(0xFF000000),
    secondaryLabel = Color(0x993C3C43),
    tertiaryLabel = Color(0x4D3C3C43),
    blue = Color(0xFF007AFF),
    red = Color(0xFFFF3B30),
    yellow = Color(0xFFFFCC00),
    cellHighlight = Color(0xFFD1D1D6),
    glassButton = Color(0xFFFFFFFF),
    isDark = false,
)

val DarkCupertinoColors = CupertinoColors(
    systemBackground = Color(0xFF000000),
    secondaryBackground = Color(0xFF1C1C1E),
    separator = Color(0xFF38383A),
    label = Color(0xFFFFFFFF),
    secondaryLabel = Color(0x99EBEBF5),
    tertiaryLabel = Color(0x4DEBEBF5),
    blue = Color(0xFF0A84FF),
    red = Color(0xFFFF453A),
    yellow = Color(0xFFFFD60A),
    cellHighlight = Color(0xFF2C2C2E),
    glassButton = Color(0xFF2C2C2E),
    isDark = true,
)

val LocalCupertinoColors = staticCompositionLocalOf { LightCupertinoColors }

/** Provides the iOS palette to the Cupertino version. */
@Composable
fun CupertinoTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCupertinoColors provides if (dark) DarkCupertinoColors else LightCupertinoColors,
        content = content,
    )
}
