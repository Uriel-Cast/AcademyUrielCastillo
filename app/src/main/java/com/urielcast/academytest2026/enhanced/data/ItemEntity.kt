package com.urielcast.academytest2026.enhanced.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.urielcast.academytest2026.core.model.Item

/**
 * Room representation of an [Item]. Kept separate from the domain model so the
 * database schema can evolve independently of the UI/domain layer.
 */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val creationIndex: Int,
    val name: String,
    val isFavorite: Boolean,
)

fun ItemEntity.toDomain(): Item =
    Item(id = id, creationIndex = creationIndex, name = name, isFavorite = isFavorite)

fun Item.toEntity(): ItemEntity =
    ItemEntity(id = id, creationIndex = creationIndex, name = name, isFavorite = isFavorite)
