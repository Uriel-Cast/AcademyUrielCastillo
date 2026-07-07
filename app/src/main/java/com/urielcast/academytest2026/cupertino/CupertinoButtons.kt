package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    clickable(interactionSource = interaction, indication = null, onClick = onClick)
}

@Composable
fun CupertinoIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    tint: Color = LocalCupertinoColors.current.blue,
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .noRippleClickable(onClick)
            .padding(8.dp)
            .size(26.dp),
    )
}

@Composable
fun CupertinoTextButton(
    text: String,
    onClick: () -> Unit,
    bold: Boolean = false,
    color: Color = LocalCupertinoColors.current.blue,
) {
    Text(
        text = text,
        color = color,
        fontSize = 17.sp,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        modifier = Modifier
            .noRippleClickable(onClick)
            .padding(8.dp),
    )
}

/** iOS back button: a chevron plus the previous screen's title, in system blue. */
@Composable
fun CupertinoBackButton(
    previousTitle: String,
    onClick: () -> Unit,
) {
    val colors = LocalCupertinoColors.current
    Row(
        modifier = Modifier
            .noRippleClickable(onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = previousTitle,
            tint = colors.blue,
        )
        Text(
            text = previousTitle,
            color = colors.blue,
            fontSize = 17.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
