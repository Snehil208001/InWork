package com.example.inwork.mainui.imageuploadscreen.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State to represent the UI's current condition
sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}

class ImageUploadViewModel : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    fun uploadImage(context: Context, imageUri: Uri?) {
        if (imageUri == null) {
            Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        // Set state to Uploading
        _uploadState.value = UploadState.Uploading

        viewModelScope.launch {
            try {
                // --- Your actual upload logic would go here ---
                // For demonstration, we simulate a network delay.
                // In a real app, this would be a Retrofit/Ktor call.
                kotlinx.coroutines.delay(2000)

                // On success, update the state
                _uploadState.value = UploadState.Success

            } catch (e: Exception) {
                // On failure, update the state with an error message
                _uploadState.value = UploadState.Error("Upload failed: ${e.message}")
            }
        }
    }

    // Function to reset the state, e.g., after the user navigates away
    fun resetState() {
        _uploadState.value = UploadState.Idle
    }
}