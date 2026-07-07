package com.urielcast.academytest2026.enhanced.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.urielcast.academytest2026.AcademyApplication
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.core.model.sortedForDisplay
import com.urielcast.academytest2026.enhanced.data.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Drives the Enhanced version. Room is the single source of truth: [uiState]
 * combines the persisted items with the transient search query and filter, so
 * the list, header stats and detail all stay consistent and survive restarts.
 */
class EnhancedItemsViewModel(private val repository: ItemsRepository) : ViewModel() {

    private val query = MutableStateFlow("")
    private val filter = MutableStateFlow(ItemFilter.ALL)

    init {
        viewModelScope.launch { repository.ensureSeeded() }
    }

    val uiState: StateFlow<EnhancedUiState> =
        combine(repository.observeItems(), query, filter) { items, currentQuery, currentFilter ->
            val visible = items
                .filter { currentFilter == ItemFilter.ALL || it.isFavorite }
                .filter { currentQuery.isBlank() || it.name.contains(currentQuery.trim(), ignoreCase = true) }
                .sortedForDisplay()

            EnhancedUiState(
                items = visible,
                totalCount = items.size,
                favoriteCount = items.count { it.isFavorite },
                query = currentQuery,
                filter = currentFilter,
                isLoading = false,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EnhancedUiState(),
        )

    /** Raw stream for the detail screen — independent of the list's filter. */
    fun observeItem(id: String): Flow<Item?> = repository.observeItem(id)

    fun setQuery(value: String) { query.value = value }

    fun setFilter(value: ItemFilter) { filter.value = value }

    fun addItem(name: String) {
        viewModelScope.launch { repository.addItem(name.trim()) }
    }

    fun toggleFavorite(item: Item) {
        viewModelScope.launch { repository.setFavorite(item.id, !item.isFavorite) }
    }

    fun rename(id: String, name: String) {
        val trimmed = name.trim()
        if (trimmed.isNotEmpty()) viewModelScope.launch { repository.rename(id, trimmed) }
    }

    fun delete(item: Item) {
        viewModelScope.launch { repository.delete(listOf(item.id)) }
    }

    /** Restores a previously deleted item — powers the Undo snackbar. */
    fun restore(item: Item) {
        viewModelScope.launch { repository.restore(listOf(item)) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    as AcademyApplication
                EnhancedItemsViewModel(application.container.itemsRepository)
            }
        }
    }
}
