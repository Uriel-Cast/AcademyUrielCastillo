package com.urielcast.academytest2026.minimal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.urielcast.academytest2026.core.model.DefaultItems
import com.urielcast.academytest2026.core.model.sortedForDisplay
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MinimalItemsListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsTitleAndSortedItems() {
        composeRule.setContent {
            AcademyUrielCastilloTheme {
                MinimalItemsListScreen(
                    items = DefaultItems.list.sortedForDisplay(),
                    onItemClick = {},
                    onToggleFavorite = {},
                    onAddItem = {},
                )
            }
        }

        composeRule.onNodeWithText("Oggetti").assertIsDisplayed()
        composeRule.onNodeWithText("Giraffa 🦒").assertIsDisplayed()
        composeRule.onNodeWithText("Lupo 🐺").assertIsDisplayed()
    }

    @Test
    fun addingAnItemReportsTheTrimmedName() {
        var savedName: String? = null
        composeRule.setContent {
            AcademyUrielCastilloTheme {
                MinimalItemsListScreen(
                    items = DefaultItems.list.sortedForDisplay(),
                    onItemClick = {},
                    onToggleFavorite = {},
                    onAddItem = { savedName = it },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Aggiungi oggetto").performClick()
        composeRule.onNodeWithText("Nome").performTextInput("Alpaca")
        composeRule.onNodeWithText("Salva").performClick()

        assertEquals("Alpaca", savedName)
    }
}
