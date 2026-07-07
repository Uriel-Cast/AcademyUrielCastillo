package com.urielcast.academytest2026.enhanced.data

import com.urielcast.academytest2026.core.model.DefaultItems
import com.urielcast.academytest2026.core.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class RoomItemsRepository(private val dao: ItemDao) : ItemsRepository {

    override fun observeItems(): Flow<List<Item>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeItem(id: String): Flow<Item?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun ensureSeeded() {
        if (dao.count() == 0) {
            dao.insertAll(DefaultItems.list.map { it.toEntity() })
        }
    }

    override suspend fun addItem(name: String): Item {
        val nextIndex = (dao.maxCreationIndex() ?: -1) + 1
        val item = Item(
            id = UUID.randomUUID().toString(),
            creationIndex = nextIndex,
            name = name,
            isFavorite = false,
        )
        dao.insert(item.toEntity())
        return item
    }

    override suspend fun addAll(names: List<String>): List<Item> {
        if (names.isEmpty()) return emptyList()

        var nextIndex = (dao.maxCreationIndex() ?: -1) + 1
        val items = names.map { name ->
            Item(
                id = UUID.randomUUID().toString(),
                creationIndex = nextIndex++,
                name = name.trim(),
                isFavorite = false,
            )
        }
        dao.insertAll(items.map { it.toEntity() })
        return items
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean) =
        dao.setFavorite(id, isFavorite)

    override suspend fun rename(id: String, name: String) =
        dao.rename(id, name)

    override suspend fun delete(ids: Collection<String>) =
        dao.deleteByIds(ids.toList())

    override suspend fun restore(items: List<Item>) =
        dao.insertAll(items.map { it.toEntity() })
}
