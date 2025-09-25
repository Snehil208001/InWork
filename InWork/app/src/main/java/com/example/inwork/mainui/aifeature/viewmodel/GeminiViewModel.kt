package com.example.inwork.mainui.viewmodels // Or your correct package name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.example.inwork.BuildConfig // Make sure this import is correct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel : ViewModel() {

    private val _responseText = MutableStateFlow("")
    val responseText = _responseText.asStateFlow()

    fun generateContent(prompt: String) {
        _responseText.value = "" // Clear previous response
        viewModelScope.launch {
            try {
                val generativeModel = GenerativeModel(
                    // Change this line
                    modelName = "gemini-1.5-flash",
                    apiKey = BuildConfig.apiKey
                )

                val response = generativeModel.generateContent(prompt)
                _responseText.value = response.text ?: "No response from API"
            } catch (e: Exception) {
                _responseText.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}