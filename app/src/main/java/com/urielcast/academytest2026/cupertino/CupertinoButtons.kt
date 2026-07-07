package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

/**
 * Circular "liquid glass" toolbar button (iOS 26 style): the icon sits on a
 * floating rounded fill with a soft shadow, as in the current iOS navigation bar.
 */
@Composable
fun CupertinoGlassIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    val colors = LocalCupertinoColors.current
    Box(
        modifier = Modifier
            .noRippleClickable(onClick)
            .shadow(elevation = 10.dp, shape = CircleShape, clip = false)
            .clip(CircleShape)
            .background(colors.glassButton)
            .size(38.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = colors.label,
            modifier = Modifier.size(22.dp),
        )
    }
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
