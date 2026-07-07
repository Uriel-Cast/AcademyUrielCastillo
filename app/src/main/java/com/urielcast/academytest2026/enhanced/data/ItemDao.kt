package com.urielcast.academytest2026.enhanced.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    /**
     * Reactive stream of all items. Room re-emits automatically on every write,
     * which is what makes changes "reflect throughout the app" — the same
     * guarantee the SwiftUI `@Observable` model gives, but backed by the DB.
     */
    @Query("SELECT * FROM items")
    fun observeAll(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<ItemEntity?>

    @Insert
    suspend fun insert(item: ItemEntity)

    @Insert
    suspend fun insertAll(items: List<ItemEntity>)

    @Update
    suspend fun update(item: ItemEntity)

    @Query("UPDATE items SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE items SET name = :name WHERE id = :id")
    suspend fun rename(id: String, name: String)

    @Query("DELETE FROM items WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT MAX(creationIndex) FROM items")
    suspend fun maxCreationIndex(): Int?

    @Query("SELECT COUNT(*) FROM items")
    suspend fun count(): Int
}
