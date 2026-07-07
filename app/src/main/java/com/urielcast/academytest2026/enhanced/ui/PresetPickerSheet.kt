package com.urielcast.academytest2026.enhanced.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R
import com.urielcast.academytest2026.enhanced.data.ItemPreset
import com.urielcast.academytest2026.enhanced.data.ItemPresets

/**
 * Bottom sheet that lets the user load a curated [ItemPreset] in one tap. Names
 * already present are skipped by the ViewModel, and the load is undoable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetPickerSheet(
    onSelect: (ItemPreset) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            Text(
                text = stringResource(R.string.enh_presets_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            )
            ItemPresets.all.forEach { preset ->
                ListItem(
                    headlineContent = { Text(stringResource(preset.titleRes)) },
                    supportingContent = {
                        Text(pluralStringResource(R.plurals.enh_item_count, preset.size, preset.size))
                    },
                    leadingContent = {
                        Text(text = preset.emoji, style = MaterialTheme.typography.headlineSmall)
                    },
                    modifier = Modifier.clickable { onSelect(preset) },
                )
            }
        }
    }
}
