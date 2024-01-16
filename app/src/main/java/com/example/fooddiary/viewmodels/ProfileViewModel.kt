package com.example.fooddiary.viewmodels

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fooddiary.data.meProfile

class ProfileViewModel : ViewModel() {

    private val _userData = MutableLiveData<ProfileScreenState>()
    val userData: LiveData<ProfileScreenState> = _userData

    fun setMe(){
        _userData.value = meProfile
    }

}


data class ProfileScreenState(
    val firstName : String,
    val surName : String
) {
}