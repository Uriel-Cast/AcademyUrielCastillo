package com.urielcast.academytest2026.testutil

import com.urielcast.academytest2026.core.model.DefaultItems
import com.urielcast.academytest2026.core.model.Item
import com.urielcast.academytest2026.enhanced.data.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * In-memory [ItemsRepository] for ViewModel tests — same reactive contract as
 * the Room implementation, without a database.
 */
class FakeItemsRepository(initial: List<Item> = emptyList()) : ItemsRepository {

    private val items = MutableStateFlow(initial)
    private var nextIndex = (initial.maxOfOrNull { it.creationIndex } ?: -1) + 1

    override fun observeItems(): Flow<List<Item>> = items

    override fun observeItem(id: String): Flow<Item?> =
        items.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun ensureSeeded() {
        if (items.value.isEmpty()) items.value = DefaultItems.list
    }

    override suspend fun addItem(name: String): Item {
        val item = Item(id = "id-${nextIndex}", creationIndex = nextIndex++, name = name, isFavorite = false)
        items.update { it + item }
        return item
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        items.update { list -> list.map { if (it.id == id) it.copy(isFavorite = isFavorite) else it } }
    }

    override suspend fun rename(id: String, name: String) {
        items.update { list -> list.map { if (it.id == id) it.copy(name = name) else it } }
    }

    override suspend fun delete(ids: Collection<String>) {
        items.update { list -> list.filterNot { it.id in ids } }
    }

    override suspend fun restore(items: List<Item>) {
        this.items.update { it + items }
    }
}
