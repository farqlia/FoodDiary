package com.example.fooddiary.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {

    private val firstName = stringPreferencesKey("first_name")
    private val surName = stringPreferencesKey("sur_name")
    private val description = stringPreferencesKey("description")

    private val imageUri = stringPreferencesKey("image_uri")

    val _userData = MutableLiveData<ProfileScreenState>()
    val _profileImage = MutableLiveData<String>()

    fun request() {
        viewModelScope.launch {
            dataStore.data.collectLatest {
                _userData.value = ProfileScreenState(it[firstName] ?: "", it[surName] ?: "", it[description] ?: "")
                _profileImage.value = it[imageUri]
            }
        }
    }

}


data class ProfileScreenState(
    val firstName : String,
    val surName : String,
    val description : String
)