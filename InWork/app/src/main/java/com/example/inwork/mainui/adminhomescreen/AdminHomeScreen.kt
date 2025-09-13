package com.example.inwork.mainui.homescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.utils.navigationbar.InWorkBottomAppBar
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar

@Composable
fun AdminHomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            InWorkTopAppBar(onNavigationIconClick = { /*TODO: Handle drawer click*/ })
        },
        bottomBar = {
            InWorkBottomAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.Black,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
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
        // CHANGED: Content is now wrapped in a Column that fills the available space.
        Column(
            modifier = Modifier
                .padding(innerPadding) // Apply the padding from the Scaffold
                .fillMaxSize()       // Tell the Column to take up all available space
                .background(Color.White) // Set a background color for the content area
        ) {
            // All your screen content goes inside this Column
            Text(
                text = "Admin Home Screen Content",
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AdminHomeScreenPreview() {
    AdminHomeScreen(navController = rememberNavController())
}