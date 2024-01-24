package com.example.fooddiary.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val PREFERENCES_NAME = "app_settings"

val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

val fonts = listOf("Default", "Roboto")

data class AppSettings(
    val darkMode: Boolean,
    val selectedFont: String,
    val fontSize: Int
)

class AppSettingsManager(context: Context) {

    private val dataStore = context.dataStore

    val appSettingsFlow: Flow<AppSettings> = dataStore.data
        .map { preferences ->
            val darkMode = preferences[PreferencesKeys.DARK_MODE] ?: false
            val selectedFont = preferences[PreferencesKeys.SELECTED_FONT] ?: "Default"
            val fontSize = preferences[PreferencesKeys.FONT_SIZE] ?: 16
            AppSettings(darkMode, selectedFont, fontSize)
        }

    suspend fun updateDarkMode(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = darkMode
        }
    }

    suspend fun updateSelectedFont(selectedFont: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_FONT] = selectedFont
        }
    }

    suspend fun updateFontSize(fontSize: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE] = fontSize
        }
    }

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SELECTED_FONT = stringPreferencesKey("selected_font")
        val FONT_SIZE = intPreferencesKey("font_size")
    }
}

@Composable
fun AppSettingsScreen(appSettingsManager: AppSettingsManager) {
    var darkMode by remember { mutableStateOf(false) }
    var selectedFont by remember { mutableStateOf("Default") }
    var fontSize by remember { mutableStateOf(16) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(appSettingsManager.appSettingsFlow) {
        appSettingsManager.appSettingsFlow.collect { appSettings ->
            darkMode = appSettings.darkMode
            selectedFont = appSettings.selectedFont
            fontSize = appSettings.fontSize
        }
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(0.8f)
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = null,
                        tint = if (darkMode) Color.Yellow else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = darkMode,
                        onCheckedChange = {
                            darkMode = it
                            coroutineScope.launch {
                                appSettingsManager.updateDarkMode(it)
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.FontDownload,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Font:")
                    Spacer(modifier = Modifier.width(8.dp))

                    Box {
                        IconButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.FilterList, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.width(200.dp),
                        ) {
                            fonts.forEach { font ->
                                DropdownMenuItem(text = {Text(font)} ,
                                    onClick = {
                                        selectedFont = font
                                        expanded = false
                                        coroutineScope.launch {
                                            appSettingsManager.updateSelectedFont(selectedFont)
                                        }},)
                            }
                        }
                    }


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Font Size:")
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = fontSize.toFloat(),
                        onValueChange = {
                            fontSize = it.toInt()
                            coroutineScope.launch {
                                appSettingsManager.updateFontSize(fontSize)
                            }
                        },
                        valueRange = 8f..24f,
                        steps = 8
                    )
                }
            }
        }
}