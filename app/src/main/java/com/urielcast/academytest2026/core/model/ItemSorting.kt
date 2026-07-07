package com.urielcast.academytest2026.core.model

import java.text.Collator

/**
 * Ordering used everywhere items are displayed.
 *
 * This is the Android equivalent of the SwiftUI comparator:
 *
 * ```swift
 * items.sorted { first, second in
 *     let nameComparison = first.name.localizedCaseInsensitiveCompare(second.name)
 *     if nameComparison == .orderedSame { return first.creationIndex < second.creationIndex }
 *     return nameComparison == .orderedAscending
 * }
 * ```
 *
 * We use a [Collator] instead of [String.compareTo] so the comparison is
 * locale-aware and case-insensitive, matching `localizedCaseInsensitiveCompare`
 * (e.g. "Álgebra" sorts next to "algebra", not after "z"). [Collator.SECONDARY]
 * ignores case while still respecting accents. When two names compare equal,
 * [Item.creationIndex] breaks the tie so the order is always stable.
 */
fun itemDisplayComparator(): Comparator<Item> {
    val collator = Collator.getInstance().apply { strength = Collator.SECONDARY }
    return Comparator<Item> { a, b -> collator.compare(a.name, b.name) }
        .thenBy { it.creationIndex }
}

/** Returns the items in display order. Does not mutate the receiver. */
fun List<Item>.sortedForDisplay(): List<Item> = sortedWith(itemDisplayComparator())
