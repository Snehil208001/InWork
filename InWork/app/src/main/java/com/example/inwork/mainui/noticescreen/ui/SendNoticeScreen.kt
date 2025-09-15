package com.example.inwork.mainui.noticescreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SendNoticeScreen(onSendNotice: (title: String, notice: String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var notice by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    var isNoticeError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                isTitleError = it.isBlank()
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = isTitleError,
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        if (isTitleError) {
            Text(
                text = "Required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = notice,
            onValueChange = {
                notice = it
                isNoticeError = it.isBlank()
            },
            label = { Text("Notice...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            isError = isNoticeError,
            shape = RoundedCornerShape(8.dp)
        )
        if (isNoticeError) {
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
                isTitleError = title.isBlank()
                isNoticeError = notice.isBlank()
                if (!isTitleError && !isNoticeError) {
                    onSendNotice(title, notice)
                }
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