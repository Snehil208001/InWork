package com.example.inwork.mainui.noticescreen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SendNoticeUiState(
    val title: String = "",
    val notice: String = "",
    val isTitleError: Boolean = false,
    val isNoticeError: Boolean = false
)

sealed class SendNoticeEvent {
    data class TitleChanged(val title: String) : SendNoticeEvent()
    data class NoticeChanged(val notice: String) : SendNoticeEvent()
    object SendNoticeClicked : SendNoticeEvent()
}

class SendNoticeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SendNoticeUiState())
    val uiState: StateFlow<SendNoticeUiState> = _uiState.asStateFlow()

    fun onEvent(event: SendNoticeEvent) {
        when (event) {
            is SendNoticeEvent.TitleChanged -> {
                _uiState.update { it.copy(title = event.title, isTitleError = event.title.isBlank()) }
            }
            is SendNoticeEvent.NoticeChanged -> {
                _uiState.update { it.copy(notice = event.notice, isNoticeError = event.notice.isBlank()) }
            }
            SendNoticeEvent.SendNoticeClicked -> {
                val state = _uiState.value
                val hasError = state.title.isBlank() || state.notice.isBlank()
                if (!hasError) {
                    // onSendNotice(state.title, state.notice)
                } else {
                    _uiState.update {
                        it.copy(
                            isTitleError = state.title.isBlank(),
                            isNoticeError = state.notice.isBlank()
                        )
                    }
                }
            }
        }
    }
}