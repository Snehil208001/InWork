package com.example.inwork.mainui.aifeature.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.inwork.mainui.viewmodels.GeminiViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraggableAIFab(geminiViewModel: GeminiViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .padding(16.dp) // Add some padding so it doesn't stick to the edges
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        FloatingActionButton(
            onClick = { showBottomSheet = true },
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Filled.AutoAwesome, contentDescription = "AI Assistant")
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState(),
        ) {
            AIPromptUI(geminiViewModel = geminiViewModel)
        }
    }
}

@Composable
fun AIPromptUI(geminiViewModel: GeminiViewModel) {
    val response by geminiViewModel.responseText.collectAsState()
    var prompt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Typing effect for the response
    var displayedResponse by remember { mutableStateOf("") }
    LaunchedEffect(response) {
        if (!isLoading) {
            response.forEachIndexed { index, char ->
                displayedResponse = response.substring(0, index + 1)
                delay(20) // Adjust delay for typing speed
            }
        }
    }

    LaunchedEffect(response) {
        isLoading = false
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 400.dp), // Set min and max height
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title and close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ask Gemini", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(Modifier.height(16.dp))

        // Response area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(16.dp)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.CenterStart
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (displayedResponse.isBlank()) {
                Text(
                    text = "Ask me anything...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = displayedResponse,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        // Prompt input field and send button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Enter your prompt") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        isLoading = true
                        displayedResponse = "" // Clear previous response immediately
                        geminiViewModel.generateContent(prompt)
                    }
                },
                enabled = prompt.isNotBlank() && !isLoading,
                modifier = Modifier.height(56.dp) // Match the height of the TextField
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send")
            }
        }
    }
}