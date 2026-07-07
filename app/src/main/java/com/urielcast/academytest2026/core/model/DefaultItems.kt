package com.urielcast.academytest2026.core.model

/**
 * Seed data shown on first launch, mirroring the SwiftUI `defaultItems`
 * (Lupo / Giraffa / Leone). Ids are fixed rather than random so previews and
 * tests are deterministic.
 */
object DefaultItems {
    val list: List<Item> = listOf(
        Item(id = "seed-lupo", creationIndex = 0, name = "Lupo 🐺", isFavorite = true),
        Item(id = "seed-giraffa", creationIndex = 1, name = "Giraffa 🦒", isFavorite = false),
        Item(id = "seed-leone", creationIndex = 2, name = "Leone 🦁", isFavorite = false),
    )
}
