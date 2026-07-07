package com.urielcast.academytest2026.enhanced.ui

import com.urielcast.academytest2026.core.model.Item

/** Which items are shown in the list. */
enum class ItemFilter { ALL, FAVORITES }

/**
 * Single immutable snapshot of everything the Enhanced list screen renders.
 * [items] is already filtered (by [query]/[filter]) and sorted for display; the
 * counts reflect the full, unfiltered set for the header stats.
 */
data class EnhancedUiState(
    val items: List<Item> = emptyList(),
    val totalCount: Int = 0,
    val favoriteCount: Int = 0,
    val query: String = "",
    val filter: ItemFilter = ItemFilter.ALL,
    val isLoading: Boolean = true,
) {
    val isEmpty: Boolean get() = items.isEmpty()
    val isFiltering: Boolean get() = query.isNotBlank() || filter != ItemFilter.ALL
}
