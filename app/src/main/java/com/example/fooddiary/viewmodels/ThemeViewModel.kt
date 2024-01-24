package com.example.fooddiary.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val forceDarkModeKey = booleanPreferencesKey("dark_mode")
    private val fontFamilyKey = stringPreferencesKey("font_family")

    val darkTheme = MutableLiveData<Boolean?>(null)
    val fontFamily = MutableLiveData<String?>(null)

    fun request() {
        viewModelScope.launch {
            dataStore.data.collectLatest {
                darkTheme.value = it[forceDarkModeKey]
                fontFamily.value = it[fontFamilyKey]
            }
        }
    }

    fun switchToUseSystemSettings(isSystemSettings: Boolean) {
        viewModelScope.launch {
            if (isSystemSettings) {
                dataStore.edit {
                    it.remove(forceDarkModeKey)
                }
            }
        }
    }

    fun switchToUseDarkMode(isDarkTheme: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[forceDarkModeKey] = isDarkTheme
            }
        }
    }
}