package com.example.inwork.mainui.authenticationscreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    // State variables for each text field
    var companyId by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var managingDirector by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create New Admin",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // White background for the top bar
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply padding from Scaffold
                    .padding(horizontal = 16.dp), // Add horizontal padding for the content
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Input Fields Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Takes up available space
                        .padding(top = 16.dp), // Padding from the top bar
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Space between text fields
                ) {
                    SignUpInputField(value = companyId, onValueChange = { companyId = it }, label = "Company ID")
                    SignUpInputField(value = companyName, onValueChange = { companyName = it }, label = "Company Name")
                    SignUpInputField(value = industry, onValueChange = { industry = it }, label = "Industry")
                    SignUpInputField(value = managingDirector, onValueChange = { managingDirector = it }, label = "Managing director")
                    SignUpInputField(value = mobile, onValueChange = { mobile = it }, label = "Mobile")
                    SignUpInputField(value = email, onValueChange = { email = it }, label = "Email")
                    SignUpInputField(value = city, onValueChange = { city = it }, label = "City")
                }

                // Add Admin Button
                Button(
                    onClick = {
                        // Handle add admin logic here
                        println("Company ID: $companyId")
                        println("Company Name: $companyName")
                        println("Industry: $industry")
                        println("Managing Director: $managingDirector")
                        println("Mobile: $mobile")
                        println("Email: $email")
                        println("City: $city")
                        // You might navigate back or to another screen
                        // navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(bottom = 16.dp), // Padding from the bottom edge
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D)) // Green button color
                ) {
                    Text("ADD ADMIN", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun SignUpInputField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true, // Ensures text stays on one line
        shape = RoundedCornerShape(8.dp), // Slightly rounded corners for the text fields
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray, // Example: Focused border color
            unfocusedBorderColor = Color.LightGray, // Example: Unfocused border color
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    // Provide a dummy NavController for the preview
    SignUpScreen(navController = rememberNavController())
}

