package com.urielcast.academytest2026.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ItemSortingTest {

    private fun item(index: Int, name: String, favorite: Boolean = false) =
        Item(id = "id-$index", creationIndex = index, name = name, isFavorite = favorite)

    @Test
    fun `sorts by name ignoring case`() {
        val items = listOf(
            item(0, "banana"),
            item(1, "Apple"),
            item(2, "cherry"),
        )

        val names = items.sortedForDisplay().map { it.name }

        assertEquals(listOf("Apple", "banana", "cherry"), names)
    }

    @Test
    fun `breaks ties by creationIndex when names are equal`() {
        val items = listOf(
            item(2, "Casa"),
            item(0, "casa"),
            item(1, "CASA"),
        )

        // All three names compare equal (case-insensitive), so the original
        // creation order must be preserved deterministically.
        val order = items.sortedForDisplay().map { it.creationIndex }

        assertEquals(listOf(0, 1, 2), order)
    }

    @Test
    fun `is locale aware for accented names`() {
        val items = listOf(
            item(0, "Zebra"),
            item(1, "Álgebra"),
            item(2, "banana"),
        )

        // "Álgebra" must sort next to the letter A, not after Z.
        val names = items.sortedForDisplay().map { it.name }

        assertEquals(listOf("Álgebra", "banana", "Zebra"), names)
    }

    @Test
    fun `does not mutate the original list`() {
        val original = listOf(item(0, "b"), item(1, "a"))

        original.sortedForDisplay()

        assertEquals(listOf("b", "a"), original.map { it.name })
    }
}
