package com.example.inwork.mainui.profilescreen.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    val profileImageUri = mutableStateOf<Uri?>(null)
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val companyId = mutableStateOf("")
    val department = mutableStateOf("")
    val designation = mutableStateOf("")
    val phone = mutableStateOf("")
    val location = mutableStateOf("")

    fun onProfileImageChange(uri: Uri?) {
        profileImageUri.value = uri
    }
}