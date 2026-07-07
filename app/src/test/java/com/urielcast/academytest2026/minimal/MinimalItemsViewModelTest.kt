package com.urielcast.academytest2026.minimal

import com.urielcast.academytest2026.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MinimalItemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `starts with the default items in display order`() = runTest {
        val vm = MinimalItemsViewModel()
        advanceUntilIdle()

        assertEquals(
            listOf("Giraffa 🦒", "Leone 🦁", "Lupo 🐺"),
            vm.items.value.map { it.name },
        )
    }

    @Test
    fun `adding trims the name and inserts it in order`() = runTest {
        val vm = MinimalItemsViewModel()

        vm.addItem("  Alpaca  ")
        advanceUntilIdle()

        assertEquals("Alpaca", vm.items.value.first().name)
    }

    @Test
    fun `blank names are ignored`() = runTest {
        val vm = MinimalItemsViewModel()
        val countBefore = vm.items.value.size

        vm.addItem("   ")
        advanceUntilIdle()

        assertEquals(countBefore, vm.items.value.size)
    }

    @Test
    fun `toggling favorite flips the flag`() = runTest {
        val vm = MinimalItemsViewModel()
        val giraffa = vm.items.value.first { it.name.startsWith("Giraffa") }

        vm.toggleFavorite(giraffa.id)
        advanceUntilIdle()

        assertTrue(vm.items.value.first { it.id == giraffa.id }.isFavorite)
    }

    @Test
    fun `deleting removes the item`() = runTest {
        val vm = MinimalItemsViewModel()
        val giraffa = vm.items.value.first { it.name.startsWith("Giraffa") }

        vm.delete(giraffa.id)
        advanceUntilIdle()

        assertFalse(vm.items.value.any { it.id == giraffa.id })
    }
}
