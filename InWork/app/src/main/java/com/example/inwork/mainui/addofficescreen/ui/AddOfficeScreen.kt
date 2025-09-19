package com.example.inwork.mainui.addofficescreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inwork.core.navigation.Screen
import com.example.inwork.mainui.addofficescreen.viewmodel.AddOfficeEvent
import com.example.inwork.mainui.addofficescreen.viewmodel.AddOfficeViewModel

@Composable
fun AddOfficeScreen(
    navController: NavController,
    viewModel: AddOfficeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.get<Double>("picked_latitude")?.let { lat ->
            savedStateHandle.get<Double>("picked_longitude")?.let { long ->
                viewModel.onEvent(AddOfficeEvent.OnLocationSet(lat, long))
                savedStateHandle.remove<Double>("picked_latitude")
                savedStateHandle.remove<Double>("picked_longitude")
            }
        }
    }

    if (state.formSubmitted) {
        AlertDialog(
            onDismissRequest = { /* Cannot be dismissed */ },
            title = { Text("Success") },
            text = { Text("Office has been added successfully.") },
            confirmButton = {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // This adds the necessary top padding
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add your locations, officeName and radius",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = state.latitude,
            onValueChange = { },
            label = { Text("latitude (Click set geolocation)") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = state.isLocationError
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.longitude,
            onValueChange = { },
            label = { Text("longitude (Click set geolocation)") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = state.isLocationError
        )
        if (state.isLocationError) {
            Text("Please set a location using the button below", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Screen.OfficeLocationPicker.route) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text("set geolocation", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.officeName,
            onValueChange = { viewModel.onEvent(AddOfficeEvent.OnOfficeNameChange(it)) },
            label = { Text("Office Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.isOfficeNameError,
            singleLine = true
        )
        if (state.isOfficeNameError) {
            Text("Office Name is required", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.radius,
            onValueChange = { viewModel.onEvent(AddOfficeEvent.OnRadiusChange(it)) },
            label = { Text("Radius (in meters)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.isRadiusError,
            singleLine = true
        )
        if (state.isRadiusError) {
            Text("Radius is required", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.onEvent(AddOfficeEvent.OnAddOfficeClick) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
        ) {
            Text("Add", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}