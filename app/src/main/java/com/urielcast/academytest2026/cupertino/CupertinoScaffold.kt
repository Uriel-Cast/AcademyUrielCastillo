package com.urielcast.academytest2026.cupertino

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text

private val NavBarHeight = 44.dp

/**
 * An iOS-style screen with a large title that scrolls with the content and
 * collapses into a centered inline title (with a hairline separator) once you
 * scroll past it — the signature iOS navigation-bar behavior, hand-built.
 */
@Composable
fun CupertinoScaffold(
    title: String,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    leading: @Composable () -> Unit = {},
    trailing: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    val colors = LocalCupertinoColors.current
    val density = LocalDensity.current
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val collapsed by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                listState.firstVisibleItemScrollOffset > with(density) { 40.dp.toPx() }
        }
    }
    val inlineAlpha by animateFloatAsState(if (collapsed) 1f else 0f, label = "inlineTitle")
    val barColor by animateColorAsState(
        if (collapsed) colors.systemBackground else Color.Transparent,
        label = "barColor",
    )
    val separatorColor by animateColorAsState(
        if (collapsed) colors.separator else Color.Transparent,
        label = "barSeparator",
    )

    Box(
        modifier
            .fillMaxSize()
            .background(colors.systemBackground),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = statusBarTop + NavBarHeight,
                bottom = 32.dp,
            ),
        ) {
            item {
                Text(
                    text = title,
                    color = colors.label,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                )
            }
            content()
        }

        // Overlay navigation bar (buttons always visible; inline title + hairline fade in).
        Column(
            Modifier
                .fillMaxWidth()
                .background(barColor),
        ) {
            Spacer(Modifier.height(statusBarTop))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(NavBarHeight),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    color = colors.label.copy(alpha = inlineAlpha),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 60.dp),
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    leading()
                    Spacer(Modifier.weight(1f))
                    trailing()
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.6.dp)
                    .background(separatorColor),
            )
        }
    }
}
