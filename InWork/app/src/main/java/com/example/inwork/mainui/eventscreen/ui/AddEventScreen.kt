package com.example.inwork.mainui.admineventscreen.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.admineventscreen.viewmodel.AddEventEvent
import com.example.inwork.mainui.admineventscreen.viewmodel.AddEventViewModel
import com.example.inwork.mainui.eventscreen.ui.CalendarView
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    navController: NavController,
    viewModel: AddEventViewModel = viewModel()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var eventName by remember { mutableStateOf("") }
    var isEventNameError by remember { mutableStateOf(false) }

    // The Scaffold has been removed from here.
    // This Column now becomes the root Composable for this screen's content.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { newDate -> selectedDate = newDate }
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = eventName,
            onValueChange = {
                eventName = it
                isEventNameError = it.isBlank()
            },
            label = { Text("Add Event") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isEventNameError,
            shape = RoundedCornerShape(8.dp)
        )
        if (isEventNameError) {
            Text(
                text = "Event name is required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isEventNameError = eventName.isBlank()
                if (!isEventNameError) {
                    // Pass event data to ViewModel
                    viewModel.onEvent(AddEventEvent.EventNameChanged(eventName))
                    viewModel.onEvent(AddEventEvent.EventDateChanged(selectedDate.toString()))
                    viewModel.onEvent(AddEventEvent.AddEventClicked)
                    // Potentially navigate back or show a confirmation
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)) // Vibrant green
        ) {
            Text("SAVE EVENT", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}