package com.urielcast.academytest2026.core.model

/**
 * A single item in the list.
 *
 * Mirrors the SwiftUI `ItemViewModel`: an immutable identity ([id]) and a
 * stable [creationIndex] used as a deterministic tie-breaker when two items
 * share the same display name, plus the mutable [name] and [isFavorite] state.
 *
 * This is a plain immutable data class shared by both app versions. The
 * "Minimal" version keeps a list of these in memory; the "Enhanced" version
 * persists them through Room and maps entities to this model.
 */
data class Item(
    val id: String,
    val creationIndex: Int,
    val name: String,
    val isFavorite: Boolean,
)
