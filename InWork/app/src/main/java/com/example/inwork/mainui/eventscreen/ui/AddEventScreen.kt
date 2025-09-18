package com.example.inwork.mainui.admineventscreen.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.inwork.core.utils.navigationbar.AdminBottomAppBar
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.mainui.admineventscreen.viewmodel.AddEventEvent
import com.example.inwork.mainui.admineventscreen.viewmodel.AddEventViewModel
import com.example.inwork.mainui.adminhomescreen.ui.AdminScreen
import com.example.inwork.mainui.eventscreen.ui.CalendarView
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    navController: NavController,
    viewModel: AddEventViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var eventName by remember { mutableStateOf("") }
    var isEventNameError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Using the custom TopAppBar from your project
            InWorkTopAppBar(
                title = "Add Event",
                onNavigationIconClick = {
                    scope.launch {
                        drawerState.apply { if (isClosed) open() else close() }
                    }
                }
            )
        },
        bottomBar = {
            // Using the custom BottomAppBar from your project
            AdminBottomAppBar(
                currentScreen = AdminScreen.AddEvent, // Assuming this is a valid state
                onScreenSelected = { /* Handle navigation */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                modifier = Modifier.offset(y = 60.dp),
                shape = CircleShape,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen(navController = rememberNavController())
}