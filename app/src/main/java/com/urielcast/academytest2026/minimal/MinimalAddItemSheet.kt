package com.urielcast.academytest2026.minimal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.ui.theme.AcademyUrielCastilloTheme
import androidx.compose.runtime.LaunchedEffect

/**
 * Bottom sheet used to create a new item — the Android counterpart of the
 * SwiftUI `AddItemView` sheet (medium detent, autofocused field, Save disabled
 * until the name is non-blank).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalAddItemSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        MinimalAddItemSheetContent(onCancel = onDismiss, onSave = onSave)
    }
}

@Composable
private fun MinimalAddItemSheetContent(
    onCancel: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    val canSave = name.isNotBlank()
    val focusRequester = remember { FocusRequester() }

    fun save() {
        if (name.isNotBlank()) onSave(name.trim())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = MinimalStrings.NEW_ITEM_SECTION,
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(MinimalStrings.NAME) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { save() }),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onCancel) { Text(MinimalStrings.CANCEL) }
            Button(onClick = { save() }, enabled = canSave) { Text(MinimalStrings.SAVE) }
        }
    }

    // Autofocus the field (and raise the keyboard) as soon as the sheet appears,
    // mirroring `.onAppear { isNameFieldFocused = true }` in SwiftUI.
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MinimalAddItemSheetContentPreview() {
    AcademyUrielCastilloTheme {
        MinimalAddItemSheetContent(onCancel = {}, onSave = {})
    }
}
