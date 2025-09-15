package com.example.inwork.mainui.authenticationscreen.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inwork.R
import com.example.inwork.core.navigation.Screen

// A simple enum to represent the possible roles
enum class LoginRole {
    ADMIN, EMPLOYEE
}

@Composable
fun LoginScreen(navController: NavController) {
    var selectedRole by remember { mutableStateOf(LoginRole.ADMIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    val passwordFocusRequester = remember { FocusRequester() }

    // --- Notification Permission Logic ---
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // You can handle the result here if needed (e.g., show a message)
        }
    )

    // Request notification permission when the screen is first composed
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    // --- End of Notification Permission Logic ---

    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            onDismiss = { showForgotPasswordDialog = false },
            onConfirm = { recoveryEmail ->
                println("Password recovery requested for: $recoveryEmail")
                showForgotPasswordDialog = false
            }
        )
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold for system bars
                .padding(horizontal = 16.dp)
                .imePadding() // Automatically adds padding when the keyboard is open
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.auth),
                contentDescription = "Login Illustration",
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                alignment = Alignment.Center
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "-------- Login As --------",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                RoleSelector(
                    selectedRole = selectedRole,
                    onRoleSelected = { newRole -> selectedRole = newRole }
                )
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            // Handle login on keyboard done action
                        })
                    )
                    TextButton(
                        onClick = { showForgotPasswordDialog = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Forgot Password?", color = Color.Blue)
                    }
                    if (selectedRole == LoginRole.ADMIN) {
                        val annotatedString = buildAnnotatedString {
                            append("Don't have an Admin account? ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF1976D2),
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Register Now")
                            }
                        }
                        ClickableText(
                            text = annotatedString,
                            onClick = {
                                navController.navigate(Screen.Signup.route)
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 8.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedRole == LoginRole.ADMIN) {
                        navController.navigate(Screen.adminHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.userHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
            ) {
                Text("Login", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Composable
fun ForgotPasswordDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var recoveryEmail by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Recover Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = recoveryEmail,
                    onValueChange = { recoveryEmail = it },
                    label = { Text("Recovery Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (recoveryEmail.isNotBlank()) {
                        onConfirm(recoveryEmail)
                    }
                }
            ) {
                Text("RECOVER", color = Color(0xFF009B4D))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF009B4D))
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun RoleSelector(selectedRole: LoginRole, onRoleSelected: (LoginRole) -> Unit) {
    val selectedBorderColor = Color(0xFF00C853) // A vibrant green
    val unselectedBorderColor = Color.Gray
    val selectedBgTint = Color(0x1A00C853) // A very faint green background tint (10% opacity)
    val textColor = Color.Black

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Admin Button
        OutlinedButton(
            onClick = { onRoleSelected(LoginRole.ADMIN) },
            shape = RoundedCornerShape(50),
            border = BorderStroke(
                width = 2.dp,
                color = if (selectedRole == LoginRole.ADMIN) selectedBorderColor else unselectedBorderColor
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selectedRole == LoginRole.ADMIN) selectedBgTint else Color.Transparent,
                contentColor = textColor
            ),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text("Admin")
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Employee Button
        OutlinedButton(
            onClick = { onRoleSelected(LoginRole.EMPLOYEE) },
            shape = RoundedCornerShape(50),
            border = BorderStroke(
                width = 2.dp,
                color = if (selectedRole == LoginRole.EMPLOYEE) selectedBorderColor else unselectedBorderColor
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selectedRole == LoginRole.EMPLOYEE) selectedBgTint else Color.Transparent,
                contentColor = textColor
            ),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text("Employee")
        }
    }
}