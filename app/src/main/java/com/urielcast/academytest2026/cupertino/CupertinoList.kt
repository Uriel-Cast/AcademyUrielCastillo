package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** Rounded-corner shape for a cell based on its position within a grouped list. */
fun cupertinoCellShape(isFirst: Boolean, isLast: Boolean): Shape {
    val r = 10.dp
    return RoundedCornerShape(
        topStart = if (isFirst) r else 0.dp,
        topEnd = if (isFirst) r else 0.dp,
        bottomStart = if (isLast) r else 0.dp,
        bottomEnd = if (isLast) r else 0.dp,
    )
}

/** Hairline separator, inset from the leading edge like iOS list rows. */
@Composable
fun CupertinoSeparator(startInset: Int = 16) {
    val colors = LocalCupertinoColors.current
    Box(
        Modifier
            .fillMaxWidth()
            .background(colors.secondaryBackground)
            .padding(start = startInset.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 0.6.dp)
                .background(colors.separator),
        )
    }
}

/**
 * A single iOS-style list cell: optional leading/trailing content, a tappable
 * area that highlights (no Material ripple), and an optional disclosure chevron.
 */
@Composable
fun CupertinoRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    showChevron: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable RowScope.() -> Unit,
) {
    val colors = LocalCupertinoColors.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val background = if (pressed && onClick != null) colors.cellHighlight else colors.secondaryBackground

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interaction,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            )
            .heightIn(min = 44.dp)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leading != null) {
            leading()
            Spacer(Modifier.width(10.dp))
        }
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            content()
        }
        if (trailing != null) trailing()
        if (showChevron) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.tertiaryLabel,
            )
        }
    }
}

/**
 * A grouped section for the Form-style detail screen: an optional gray header
 * and a rounded container inset from the screen edges.
 */
@Composable
fun CupertinoSection(
    modifier: Modifier = Modifier,
    header: String? = null,
    content: @Composable () -> Unit,
) {
    val colors = LocalCupertinoColors.current
    Column(modifier.padding(horizontal = 16.dp)) {
        if (header != null) {
            androidx.compose.material3.Text(
                text = header.uppercase(),
                color = colors.secondaryLabel,
                fontSize = androidx.compose.ui.unit.TextUnit(13f, androidx.compose.ui.unit.TextUnitType.Sp),
                modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
            )
        }
        Column(Modifier.clip(RoundedCornerShape(10.dp))) {
            content()
        }
    }
}
