package com.urielcast.academytest2026.cupertino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupertinoAddSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    val colors = LocalCupertinoColors.current
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.systemBackground,
    ) {
        var name by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val canSave = name.isNotBlank()

        fun save() {
            if (canSave) onSave(name.trim())
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(bottom = 24.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CupertinoTextButton(text = CupertinoStrings.CANCEL, onClick = onDismiss)
                Text(
                    text = CupertinoStrings.ADD_TITLE,
                    color = colors.label,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                CupertinoTextButton(
                    text = CupertinoStrings.SAVE,
                    onClick = { save() },
                    bold = true,
                    color = if (canSave) colors.blue else colors.tertiaryLabel,
                )
            }

            CupertinoSection(modifier = Modifier.padding(top = 8.dp)) {
                CupertinoRow {
                    Box(Modifier.fillMaxWidth()) {
                        if (name.isEmpty()) {
                            Text(
                                text = CupertinoStrings.NAME_PLACEHOLDER,
                                color = colors.tertiaryLabel,
                                fontSize = 17.sp,
                            )
                        }
                        BasicTextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = true,
                            textStyle = TextStyle(color = colors.label, fontSize = 17.sp),
                            cursorBrush = SolidColor(colors.blue),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(onDone = { save() }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                        )
                    }
                }
            }
        }

        LaunchedEffect(Unit) { focusRequester.requestFocus() }
    }
}
