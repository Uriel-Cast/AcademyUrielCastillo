package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielcast.academytest2026.core.model.Item

@Composable
fun CupertinoListScreen(
    items: List<Item>,
    onItemClick: (Item) -> Unit,
    onToggleFavorite: (Item) -> Unit,
    onAddItem: (String) -> Unit,
    onDelete: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddSheet by remember { mutableStateOf(false) }

    CupertinoScaffold(
        title = CupertinoStrings.LIST_TITLE,
        modifier = modifier,
        trailing = {
            CupertinoGlassIconButton(
                icon = Icons.Filled.Add,
                contentDescription = CupertinoStrings.ADD_TITLE,
                onClick = { showAddSheet = true },
            )
        },
    ) {
        if (items.isEmpty()) {
            item { CupertinoEmptyState() }
        } else {
            item {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(10.dp)),
                ) {
                    items.forEachIndexed { index, item ->
                        CupertinoItemCell(
                            item = item,
                            onClick = { onItemClick(item) },
                            onToggleFavorite = { onToggleFavorite(item) },
                            onDelete = { onDelete(item) },
                        )
                        if (index != items.lastIndex) CupertinoSeparator(startInset = 16)
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        CupertinoAddSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name ->
                onAddItem(name)
                showAddSheet = false
            },
        )
    }
}

@Composable
private fun CupertinoItemCell(
    item: Item,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = LocalCupertinoColors.current
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) onDelete()
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(colors.red)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(CupertinoStrings.DELETE, color = Color.White, fontSize = 17.sp)
            }
        },
    ) {
        CupertinoRow(
            onClick = onClick,
            showChevron = true,
            trailing = { CupertinoFavoriteStar(item.isFavorite, onToggleFavorite) },
        ) {
            // Mirrors the SwiftUI ItemRowView: headline name over a caption that
            // states the favorite state, with the star trailing.
            Column(Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = item.name,
                    color = colors.label,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = if (item.isFavorite) CupertinoStrings.FAVORITE else CupertinoStrings.NOT_FAVORITE,
                    color = colors.secondaryLabel,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

@Composable
fun CupertinoFavoriteStar(isFavorite: Boolean, onToggle: () -> Unit) {
    val colors = LocalCupertinoColors.current
    CupertinoIconButton(
        icon = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
        contentDescription = CupertinoStrings.FAVORITE,
        onClick = onToggle,
        tint = if (isFavorite) colors.yellow else colors.tertiaryLabel,
    )
}

@Composable
private fun CupertinoEmptyState() {
    val colors = LocalCupertinoColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = CupertinoStrings.EMPTY_TITLE,
            color = colors.label,
            fontSize = 20.sp,
        )
        Text(
            text = CupertinoStrings.EMPTY_DESCRIPTION,
            color = colors.secondaryLabel,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}
