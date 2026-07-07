package com.urielcast.academytest2026.enhanced.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.urielcast.academytest2026.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    settings: AppSettings,
    onThemeChange: (ThemeMode) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = stringResource(R.string.enh_settings),
                style = MaterialTheme.typography.titleLarge,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.enh_theme),
                    style = MaterialTheme.typography.labelLarge,
                )
                ThemeSelector(selected = settings.theme, onSelect = onThemeChange)
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.enh_language),
                    style = MaterialTheme.typography.labelLarge,
                )
                LanguageOptions(selected = settings.language, onSelect = onLanguageChange)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSelector(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
) {
    val options = listOf(
        Triple(ThemeMode.SYSTEM, R.string.enh_theme_system, Icons.Outlined.Contrast),
        Triple(ThemeMode.LIGHT, R.string.enh_theme_light, Icons.Outlined.LightMode),
        Triple(ThemeMode.DARK, R.string.enh_theme_dark, Icons.Outlined.DarkMode),
    )
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, (mode, labelRes, icon) ->
            SegmentedButton(
                selected = selected == mode,
                onClick = { onSelect(mode) },
                shape = SegmentedButtonDefaults.itemShape(index, options.size),
                icon = { Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) },
                label = { Text(stringResource(labelRes)) },
            )
        }
    }
}

@Composable
private fun LanguageOptions(
    selected: AppLanguage,
    onSelect: (AppLanguage) -> Unit,
) {
    val options = listOf(
        AppLanguage.SYSTEM to stringResource(R.string.enh_language_system),
        AppLanguage.ITALIAN to "Italiano",
        AppLanguage.ENGLISH to "English",
        AppLanguage.SPANISH to "Español",
    )
    Column {
        options.forEach { (language, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected == language,
                        onClick = { onSelect(language) },
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = selected == language, onClick = null)
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
