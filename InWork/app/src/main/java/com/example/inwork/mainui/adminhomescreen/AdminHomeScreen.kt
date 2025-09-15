package com.example.inwork.mainui.adminhomescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.utils.navigationbar.AdminBottomAppBar
import com.example.inwork.core.utils.navigationbar.AdminSideBar
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import kotlinx.coroutines.launch

// Sealed class to represent the different content states of the admin screen
sealed class AdminScreen(val title: String) {
    object Home : AdminScreen("Home")
    object AllMenu : AdminScreen("All Menu")
    object SentNotice : AdminScreen("Sent Notices")
    object Notification : AdminScreen("Notifications")
}

@Composable
fun AdminHomeScreen(navController: NavController) {
    // State for controlling the drawer (open/closed)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // State to manage the currently selected screen/content
    var currentScreen by remember { mutableStateOf<AdminScreen>(AdminScreen.Home) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Provide the AdminSideBar as the drawer's content
            ModalDrawerSheet {
                AdminSideBar(
                    companyName = "Apple",
                    email = "soumadeepbarik@gmail.com"
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                // Open the drawer when the menu icon is clicked
                InWorkTopAppBar(
                    title = currentScreen.title, // Title is now dynamic
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            bottomBar = {
                AdminBottomAppBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen ->
                        currentScreen = newScreen
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Handle FAB click */ },
                    modifier = Modifier.offset(y = 60.dp),
                    shape = CircleShape,
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
            // Conditionally display content based on the currentScreen state
            when (currentScreen) {
                is AdminScreen.Home -> {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Admin Home Screen Content",
                        )
                    }
                }
                is AdminScreen.AllMenu -> {
                    AllMenuContent(modifier = Modifier.padding(innerPadding), navController = navController)
                }
                is AdminScreen.SentNotice -> {
                    // Placeholder for Sent Notice Screen Content
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Text(text = "Sent Notice Content")
                    }
                }
                is AdminScreen.Notification -> {
                    // Placeholder for Notification Screen Content
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Text(text = "Notification Content")
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AdminHomeScreenPreview() {
    AdminHomeScreen(navController = rememberNavController())
}