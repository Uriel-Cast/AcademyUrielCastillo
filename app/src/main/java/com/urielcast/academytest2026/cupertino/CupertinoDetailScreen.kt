package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.urielcast.academytest2026.core.model.Item

/**
 * iOS Form-style detail: an "Oggetto" group with the name and a favorite toggle,
 * and a destructive delete button in its own group, mirroring the SwiftUI
 * `Form { Section("Oggetto") { … } }`. [showBackButton] is false when shown as a
 * pushed pane without its own chrome.
 */
@Composable
fun CupertinoDetailScreen(
    item: Item,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
) {
    val colors = LocalCupertinoColors.current

    CupertinoScaffold(
        title = item.name,
        modifier = modifier,
        leading = {
            if (showBackButton) {
                CupertinoBackButton(previousTitle = CupertinoStrings.LIST_TITLE, onClick = onBack)
            }
        },
    ) {
        item {
            CupertinoSection(header = CupertinoStrings.SECTION_ITEM) {
                CupertinoRow(
                    trailing = { Text(item.name, color = colors.secondaryLabel, fontSize = 17.sp) },
                ) {
                    Text(CupertinoStrings.NAME, color = colors.label, fontSize = 17.sp)
                }
                CupertinoSeparator(startInset = 16)
                CupertinoRow(
                    trailing = { CupertinoFavoriteStar(item.isFavorite, onToggleFavorite) },
                ) {
                    Text(CupertinoStrings.FAVORITE, color = colors.label, fontSize = 17.sp)
                }
            }
        }

        item { Spacer(Modifier.height(24.dp)) }

        item {
            CupertinoSection {
                CupertinoRow(onClick = onDelete) {
                    Text(
                        text = CupertinoStrings.DELETE,
                        color = colors.red,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
