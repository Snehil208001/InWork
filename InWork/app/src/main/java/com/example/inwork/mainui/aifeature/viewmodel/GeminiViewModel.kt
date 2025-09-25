package com.example.inwork.mainui.aifeature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inwork.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Data class to represent a single message in the chat
data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)

class GeminiViewModel : ViewModel() {

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun generateContent(prompt: String) {
        // Add user's message to the history
        _chatHistory.value = _chatHistory.value + ChatMessage(text = prompt, isFromUser = true)

        viewModelScope.launch {
            try {
                // Add a temporary loading message for the AI
                _chatHistory.value = _chatHistory.value + ChatMessage(text = "...", isFromUser = false)

                val response = generativeModel.generateContent(prompt)
                val aiResponse = response.text ?: "Sorry, I couldn't process that. Please try again."

                // Replace the loading message with the actual response
                _chatHistory.value = _chatHistory.value.dropLast(1) + ChatMessage(text = aiResponse, isFromUser = false)

            } catch (e: Exception) {
                val errorMessage = "Error: ${e.message ?: "An unknown error occurred"}"
                // Replace the loading message with an error message
                _chatHistory.value = _chatHistory.value.dropLast(1) + ChatMessage(text = errorMessage, isFromUser = false)
            }
        }
    }

    // Function to clear the conversation when the chat is closed
    fun clearConversation() {
        _chatHistory.value = emptyList()
    }
}