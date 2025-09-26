package com.example.inwork.mainui.authenticationscreen.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    // State variables
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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // ✅ whole content scrollable
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SignUpInputField(value = companyId, onValueChange = { companyId = it }, label = "Company ID")
                SignUpInputField(value = companyName, onValueChange = { companyName = it }, label = "Company Name")
                SignUpInputField(value = industry, onValueChange = { industry = it }, label = "Industry")
                SignUpInputField(value = managingDirector, onValueChange = { managingDirector = it }, label = "Managing director")
                SignUpInputField(value = mobile, onValueChange = { mobile = it }, label = "Mobile")
                SignUpInputField(value = email, onValueChange = { email = it }, label = "Email")
                SignUpInputField(value = city, onValueChange = { city = it }, label = "City")
            }

            // ✅ Now button is part of scrollable content
            Button(
                onClick = {
                    println("Company ID: $companyId")
                    println("Company Name: $companyName")
                    println("Industry: $industry")
                    println("Managing Director: $managingDirector")
                    println("Mobile: $mobile")
                    println("Email: $email")
                    println("City: $city")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
            ) {
                Text(
                    "ADD ADMIN",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(50.dp)) // ensures button is not cut off
        }
    }
}

@Composable
fun SignUpInputField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}
