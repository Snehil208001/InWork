package com.example.inwork.mainui.authenticationscreen.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inwork.R

// A simple enum to represent the possible roles
enum class LoginRole {
    ADMIN, EMPLOYEE
}

@Composable
fun LoginScreen(navController: NavController) {
    // State variables to hold the current UI state
    var selectedRole by remember { mutableStateOf(LoginRole.ADMIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // State variable to control the dialog's visibility
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    // This block will display the dialog when the state is true
    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            onDismiss = { showForgotPasswordDialog = false },
            onConfirm = { recoveryEmail ->
                // TODO: Add your password recovery logic here (e.g., call your backend API)
                println("Password recovery requested for: $recoveryEmail") // Placeholder action
                showForgotPasswordDialog = false // Close the dialog after confirming
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Helps push content apart vertically
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Top Illustration
        Image(
            painter = painterResource(id = R.drawable.auth),
            contentDescription = "Login Illustration",
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth(),
            alignment = Alignment.Center
        )

        // Role Selector Section
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

        // Login Form Card
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
                // Email Text Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Password Text Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle password visibility")
                        }
                    }
                )

                // Forgot Password - Aligned to the end (right side)
                TextButton(
                    onClick = { showForgotPasswordDialog = true }, // This shows the dialog
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot Password?", color = Color.Blue)
                }

                Spacer(modifier = Modifier.height(8.dp)) // Space between the two text lines

                // Register Now Text - Only shown for Admin, centered
                if (selectedRole == LoginRole.ADMIN) {
                    val annotatedString = buildAnnotatedString {
                        append("Don't have an Admin account? ")
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF1976D2), // Blue link color
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Register Now")
                        }
                    }
                    ClickableText(
                        text = annotatedString,
                        onClick = {
                            navController.navigate("signup")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        // Login Button
        Button(
            onClick = {
                navController.navigate("onboarding")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D)) // Green button color
        ) {
            Text("Login", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))
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
