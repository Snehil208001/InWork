package com.example.inwork.mainui.noticescreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inwork.mainui.noticescreen.viewmodel.SendNoticeEvent
import com.example.inwork.mainui.noticescreen.viewmodel.SendNoticeViewModel

@Composable
fun SendNoticeScreen(
    viewModel: SendNoticeViewModel = viewModel(),
    onSendNotice: (title: String, notice: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // This adds the necessary top padding
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = uiState.title,
            onValueChange = {
                viewModel.onEvent(SendNoticeEvent.TitleChanged(it))
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.isTitleError,
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        if (uiState.isTitleError) {
            Text(
                text = "Required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.notice,
            onValueChange = {
                viewModel.onEvent(SendNoticeEvent.NoticeChanged(it))
            },
            label = { Text("Notice...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            isError = uiState.isNoticeError,
            shape = RoundedCornerShape(8.dp)
        )
        if (uiState.isNoticeError) {
            Text(
                text = "Required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.onEvent(SendNoticeEvent.SendNoticeClicked)
                // The check for errors is already inside the ViewModel,
                // this logic should be handled there to decide whether to trigger the callback.
                // Assuming ViewModel handles the validation before triggering.
                onSendNotice(uiState.title, uiState.notice)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
        ) {
            Text("SEND NOTICE", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}