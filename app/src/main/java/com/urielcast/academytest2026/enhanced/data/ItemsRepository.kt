package com.urielcast.academytest2026.enhanced.data

import com.urielcast.academytest2026.core.model.Item
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for the Enhanced version. The UI never touches Room
 * directly — it observes [Flow]s here and calls suspend mutators, so the same
 * data drives the list, the detail pane, and any other observer, and every
 * change is reflected everywhere automatically.
 */
interface ItemsRepository {

    fun observeItems(): Flow<List<Item>>

    fun observeItem(id: String): Flow<Item?>

    /** Inserts the default seed data the first time the app runs. */
    suspend fun ensureSeeded()

    /** Adds a new item and returns it (name is trimmed by the caller). */
    suspend fun addItem(name: String): Item

    /**
     * Adds several items in one batch with sequential creation indices, and
     * returns them. Used to load a preset list; the returned items let the
     * caller offer Undo.
     */
    suspend fun addAll(names: List<String>): List<Item>

    suspend fun setFavorite(id: String, isFavorite: Boolean)

    suspend fun rename(id: String, name: String)

    suspend fun delete(ids: Collection<String>)

    /** Re-inserts previously deleted items verbatim — used to power Undo. */
    suspend fun restore(items: List<Item>)
}
