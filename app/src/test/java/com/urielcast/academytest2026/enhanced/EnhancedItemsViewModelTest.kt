package com.urielcast.academytest2026.enhanced

import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.enhanced.ui.EnhancedItemsViewModel
import com.urielcast.academytest2026.enhanced.ui.ItemFilter
import com.urielcast.academytest2026.testutil.FakeItemsRepository
import com.urielcast.academytest2026.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EnhancedItemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun item(index: Int, name: String, favorite: Boolean = false) =
        Item(id = "id-$index", creationIndex = index, name = name, isFavorite = favorite)

    /** Keeps the WhileSubscribed uiState hot for the duration of a test. */
    private fun kotlinx.coroutines.test.TestScope.collectUiState(vm: EnhancedItemsViewModel) {
        backgroundScope.launch { vm.uiState.collect {} }
    }

    @Test
    fun `seeds default items when the store is empty`() = runTest {
        val vm = EnhancedItemsViewModel(FakeItemsRepository())
        collectUiState(vm)
        advanceUntilIdle()

        assertEquals(3, vm.uiState.value.totalCount)
    }

    @Test
    fun `added item shows up in display order`() = runTest {
        val repo = FakeItemsRepository(listOf(item(0, "Giraffa"), item(1, "Lupo")))
        val vm = EnhancedItemsViewModel(repo)
        collectUiState(vm)

        vm.addItem("Alpaca")
        advanceUntilIdle()

        assertEquals(listOf("Alpaca", "Giraffa", "Lupo"), vm.uiState.value.items.map { it.name })
    }

    @Test
    fun `toggling favorite flips the flag and updates the count`() = runTest {
        val giraffa = item(0, "Giraffa")
        val vm = EnhancedItemsViewModel(FakeItemsRepository(listOf(giraffa)))
        collectUiState(vm)

        vm.toggleFavorite(giraffa)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.items.first { it.id == giraffa.id }.isFavorite)
        assertEquals(1, vm.uiState.value.favoriteCount)
    }

    @Test
    fun `delete then restore brings the item back (undo)`() = runTest {
        val giraffa = item(0, "Giraffa")
        val repo = FakeItemsRepository(listOf(giraffa, item(1, "Lupo", favorite = true)))
        val vm = EnhancedItemsViewModel(repo)
        collectUiState(vm)

        vm.delete(giraffa)
        advanceUntilIdle()
        assertFalse(vm.uiState.value.items.any { it.id == giraffa.id })

        vm.restore(giraffa)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.items.any { it.id == giraffa.id })
    }

    @Test
    fun `favorites filter hides non-favorites`() = runTest {
        val repo = FakeItemsRepository(listOf(item(0, "Giraffa"), item(1, "Lupo", favorite = true)))
        val vm = EnhancedItemsViewModel(repo)
        collectUiState(vm)

        vm.setFilter(ItemFilter.FAVORITES)
        advanceUntilIdle()

        assertEquals(listOf("Lupo"), vm.uiState.value.items.map { it.name })
        assertEquals(2, vm.uiState.value.totalCount)
    }

    @Test
    fun `search matches by name case-insensitively`() = runTest {
        val repo = FakeItemsRepository(listOf(item(0, "Giraffa"), item(1, "Lupo")))
        val vm = EnhancedItemsViewModel(repo)
        collectUiState(vm)

        vm.setQuery("lup")
        advanceUntilIdle()

        assertEquals(listOf("Lupo"), vm.uiState.value.items.map { it.name })
    }
}
