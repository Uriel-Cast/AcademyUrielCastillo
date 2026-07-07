package com.urielcast.academytest2026.enhanced.data

import androidx.annotation.StringRes
import com.urielcast.academytest2026.R

/**
 * A curated list the user can load into the Enhanced version in one tap, so they
 * don't have to type everything from scratch.
 *
 * The [titleRes] is localized; the [itemNames] are data (Italian + emoji, like
 * the seed items) and stay as-is regardless of the UI language.
 */
data class ItemPreset(
    val id: String,
    @StringRes val titleRes: Int,
    val emoji: String,
    val itemNames: List<String>,
) {
    val size: Int get() = itemNames.size
}

object ItemPresets {
    val all: List<ItemPreset> = listOf(
        ItemPreset(
            id = "animals",
            titleRes = R.string.enh_preset_animals,
            emoji = "🦁",
            itemNames = listOf("Elefante 🐘", "Tigre 🐯", "Pinguino 🐧", "Volpe 🦊", "Koala 🐨"),
        ),
        ItemPreset(
            id = "groceries",
            titleRes = R.string.enh_preset_groceries,
            emoji = "🛒",
            itemNames = listOf("Latte 🥛", "Pane 🍞", "Uova 🥚", "Caffè ☕", "Mele 🍎"),
        ),
        ItemPreset(
            id = "books",
            titleRes = R.string.enh_preset_books,
            emoji = "📚",
            itemNames = listOf("Il Nome della Rosa 📕", "1984 📗", "Dune 📘", "Il Piccolo Principe 📙"),
        ),
        ItemPreset(
            id = "travel",
            titleRes = R.string.enh_preset_travel,
            emoji = "🧳",
            itemNames = listOf("Passaporto 🛂", "Caricabatterie 🔌", "Spazzolino 🪥", "Occhiali 🕶️"),
        ),
    )
}
