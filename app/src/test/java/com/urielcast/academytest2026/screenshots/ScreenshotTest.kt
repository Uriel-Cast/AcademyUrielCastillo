package com.urielcast.academytest2026.screenshots

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.cupertino.CupertinoDetailScreen
import com.urielcast.academytest2026.cupertino.CupertinoListScreen
import com.urielcast.academytest2026.cupertino.CupertinoTheme
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemDetailScreen
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemsListScreen
import com.urielcast.academytest2026.enhanced.ui.EnhancedUiState
import com.urielcast.academytest2026.minimal.MinimalItemDetailScreen
import com.urielcast.academytest2026.minimal.MinimalItemsListScreen
import com.urielcast.academytest2026.shared.LauncherScreen
import com.urielcast.academytest2026.shared.ProfileScreen
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Golden-image tests. Compose screens are rendered in the JVM with Robolectric
 * and captured by Roborazzi — no device or emulator needed, so they run in CI.
 *
 *   ./gradlew recordRoborazziDebug   # record/update the goldens in src/test/screenshots
 *   ./gradlew verifyRoborazziDebug   # compare against the committed goldens
 *
 * (Chosen over the official Compose screenshot plugin, whose preview discovery
 * doesn't work on this canary AGP 9 toolchain — see the README.)
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeRule,
        captureRoot = composeRule.onRoot(),
        options = RoborazziRule.Options(),
    )

    private val sampleItems = listOf(
        Item("1", 0, "Giraffa 🦒", isFavorite = false),
        Item("2", 1, "Leone 🦁", isFavorite = false),
        Item("3", 2, "Lupo 🐺", isFavorite = true),
    )

    private val enhancedState = EnhancedUiState(
        items = sampleItems,
        totalCount = 3,
        favoriteCount = 1,
        isLoading = false,
    )

    private fun capture(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
        composeRule.setContent {
            AcademyUrielCastilloTheme(darkTheme = dark, dynamicColor = false) {
                Surface(color = MaterialTheme.colorScheme.background) { content() }
            }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }

    private fun captureCupertino(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
        composeRule.setContent {
            CupertinoTheme(dark = dark) { content() }
        }
        composeRule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }

    @Test
    fun launcher() = capture("launcher") {
        LauncherScreen(onOpenMinimal = {}, onOpenEnhanced = {}, onOpenCupertino = {}, onOpenProfile = {})
    }

    @Test
    fun launcherDark() = capture("launcher_dark", dark = true) {
        LauncherScreen(onOpenMinimal = {}, onOpenEnhanced = {}, onOpenCupertino = {}, onOpenProfile = {})
    }

    @Test
    fun profileLight() = capture("profile_light") {
        ProfileScreen(onBack = {})
    }

    @Test
    fun profileDark() = capture("profile_dark", dark = true) {
        ProfileScreen(onBack = {})
    }

    @Test
    fun minimalList() = capture("minimal_list") {
        MinimalItemsListScreen(
            items = sampleItems,
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
        )
    }

    @Test
    fun minimalEmpty() = capture("minimal_empty") {
        MinimalItemsListScreen(
            items = emptyList(),
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
        )
    }

    @Test
    fun minimalDetail() = capture("minimal_detail") {
        MinimalItemDetailScreen(
            item = Item("1", 0, "Álgebra 📐", isFavorite = true),
            onToggleFavorite = {},
            onDelete = {},
            onBack = {},
        )
    }

    @Test
    fun enhancedListLight() = capture("enhanced_list_light") { EnhancedList() }

    @Test
    fun enhancedListDark() = capture("enhanced_list_dark", dark = true) { EnhancedList() }

    @Test
    fun enhancedDetail() = capture("enhanced_detail") {
        EnhancedItemDetailScreen(
            item = Item("3", 2, "Lupo 🐺", isFavorite = true),
            onToggleFavorite = {},
            onRename = {},
            onDelete = {},
            onBack = {},
        )
    }

    @Test
    fun enhancedListItalian() {
        RuntimeEnvironment.setQualifiers("+it")
        capture("enhanced_list_it") { EnhancedList() }
    }

    @Test
    fun enhancedListSpanish() {
        RuntimeEnvironment.setQualifiers("+es")
        capture("enhanced_list_es") { EnhancedList() }
    }

    // --- Cupertino ---

    @Test
    fun cupertinoListLight() = captureCupertino("cupertino_list_light") { CupertinoList() }

    @Test
    fun cupertinoListDark() = captureCupertino("cupertino_list_dark", dark = true) { CupertinoList() }

    @Test
    fun cupertinoDetail() = captureCupertino("cupertino_detail") {
        CupertinoDetailScreen(
            item = Item("3", 2, "Lupo 🐺", isFavorite = true),
            onToggleFavorite = {},
            onDelete = {},
            onBack = {},
        )
    }

    @Composable
    private fun CupertinoList() {
        CupertinoListScreen(
            items = sampleItems,
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
            onDelete = {},
        )
    }

    @Composable
    private fun EnhancedList() {
        EnhancedItemsListScreen(
            state = enhancedState,
            snackbarHostState = remember { SnackbarHostState() },
            onItemClick = {},
            onToggleFavorite = {},
            onAddItem = {},
            onDeleteItem = {},
            onQueryChange = {},
            onFilterChange = {},
            onLoadPreset = {},
            onOpenSettings = {},
        )
    }
}
