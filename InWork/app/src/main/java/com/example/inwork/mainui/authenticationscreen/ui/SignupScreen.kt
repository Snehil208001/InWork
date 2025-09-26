package com.example.inwork.mainui.authenticationscreen.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.authenticationscreen.viewodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate away upon successful sign-up
    LaunchedEffect(uiState.signUpSuccessful) {
        if (uiState.signUpSuccessful) {
            // You might want to navigate to a success screen or back to login
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = Color(0xFFFEF7F7), // Light pink background
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
                    containerColor = Color.Transparent // Make TopAppBar transparent
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp), // Increased horizontal padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Increased spacing
            ) {
                SignUpInputField(
                    value = uiState.companyId,
                    onValueChange = { viewModel.onCompanyIdChange(it) },
                    label = "Company ID",
                    isError = uiState.companyIdError != null,
                    errorMessage = uiState.companyIdError
                )
                SignUpInputField(
                    value = uiState.companyName,
                    onValueChange = { viewModel.onCompanyNameChange(it) },
                    label = "Company Name",
                    isError = uiState.companyNameError != null,
                    errorMessage = uiState.companyNameError
                )
                SignUpInputField(
                    value = uiState.industry,
                    onValueChange = { viewModel.onIndustryChange(it) },
                    label = "Industry",
                    isError = uiState.industryError != null,
                    errorMessage = uiState.industryError
                )
                SignUpInputField(
                    value = uiState.managingDirector,
                    onValueChange = { viewModel.onDirectorChange(it) },
                    label = "Managing director",
                    isError = uiState.managingDirectorError != null,
                    errorMessage = uiState.managingDirectorError
                )
                SignUpInputField(
                    value = uiState.mobile,
                    onValueChange = { viewModel.onMobileChange(it) },
                    label = "Mobile",
                    isError = uiState.mobileError != null,
                    errorMessage = uiState.mobileError
                )
                SignUpInputField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = "Email",
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError
                )
                SignUpInputField(
                    value = uiState.city,
                    onValueChange = { viewModel.onCityChange(it) },
                    label = "City",
                    isError = uiState.cityError != null,
                    errorMessage = uiState.cityError
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onAddAdminClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
            ) {
                Text(
                    "ADD ADMIN",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SignUpInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                // Apply the background color and shape directly to the modifier
                .background(Color.White, RoundedCornerShape(12.dp)),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            // REMOVED containerColor from here
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE91E63), // Pink border when focused
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                cursorColor = Color.Black,
                // Set the container color to transparent to let the modifier's background show through
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            isError = isError
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignupScreen(navController = rememberNavController())
}