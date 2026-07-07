package com.urielcast.academytest2026.cupertino

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
 * In-memory store for the Cupertino version — the same behavior as the Minimal
 * version, kept separate so the Minimal port stays untouched.
 */
class CupertinoItemsViewModel : ViewModel() {

    private val _items = MutableStateFlow(DefaultItems.list)

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
        _items.update {
            it + Item(UUID.randomUUID().toString(), nextCreationIndex++, trimmed, isFavorite = false)
        }
    }

    fun toggleFavorite(id: String) {
        _items.update { list -> list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it } }
    }

    fun delete(id: String) {
        _items.update { list -> list.filterNot { it.id == id } }
    }
}
