package com.urielcast.academytest2026.minimal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.urielcast.academytest2026.core.model.DefaultItems
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.core.model.sortedForDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * In-memory store for the Minimal version, mirroring SwiftUI's
 * `ItemsListViewModel`: a mutable collection of items exposed in display order,
 * with a monotonically increasing `creationIndex` for newly added items.
 *
 * State lives in a [MutableStateFlow], so the list and the detail screen both
 * observe the same source and stay in sync automatically — the Compose
 * equivalent of SwiftUI's `@Observable`. There is no persistence here, exactly
 * like the original app; the Enhanced version adds Room.
 */
class MinimalItemsViewModel : ViewModel() {

    private val _items = MutableStateFlow(DefaultItems.list)

    /** Items already sorted for display (name, then creationIndex). */
    val items: StateFlow<List<Item>> = _items
        .map { it.sortedForDisplay() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DefaultItems.list.sortedForDisplay(),
        )

    private var nextCreationIndex =
        (DefaultItems.list.maxOfOrNull { it.creationIndex } ?: -1) + 1

    fun addItem(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return

        val newItem = Item(
            id = UUID.randomUUID().toString(),
            creationIndex = nextCreationIndex++,
            name = trimmed,
            isFavorite = false,
        )
        _items.update { it + newItem }
    }

    fun toggleFavorite(id: String) {
        _items.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun delete(id: String) {
        _items.update { list -> list.filterNot { it.id == id } }
    }
}
