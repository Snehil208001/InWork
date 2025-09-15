package com.example.inwork.mainui.userhomescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.core.utils.navigationbar.UserBottomAppBar
import com.example.inwork.core.utils.navigationbar.UserSideBar
import com.example.inwork.mainui.notificationscreen.NotificationScreen
import com.example.inwork.mainui.userhomescreen.viewmodel.UserHomeEvent
import com.example.inwork.mainui.userhomescreen.viewmodel.UserHomeViewModel
import kotlinx.coroutines.launch

// Represents the different screens accessible from the user's bottom navigation bar
sealed class UserScreen(val title: String) {
    object Home : UserScreen("Home")
    object AddGeo : UserScreen("Add Geo")
    object Notices : UserScreen("Notices")
    object Notification : UserScreen("Notifications")
}

@Composable
fun UserHomeScreen(
    navController: NavController,
    viewModel: UserHomeViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val currentScreen = state.currentScreen

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                UserSideBar(
                    userName = "Ratan",
                    email = "soumadeepbarik2001@gmail.com"
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                InWorkTopAppBar(
                    title = currentScreen.title,
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            },
            bottomBar = {
                UserBottomAppBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen ->
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(newScreen))
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Handle SOS click */ },
                    modifier = Modifier.offset(y = 60.dp),
                    shape = FloatingActionButtonDefaults.extendedFabShape,
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Text(text = "SOS")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Display content based on the selected screen from the view model state
                when (currentScreen) {
                    is UserScreen.Home -> {
                        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                            Text(text = "User Home Screen Content")
                        }
                    }
                    is UserScreen.AddGeo -> {
                        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                            Text(text = "Add Geofence Screen Content")
                        }
                    }
                    is UserScreen.Notices -> {
                        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                            Text(text = "Notices Screen Content")
                        }
                    }
                    is UserScreen.Notification -> {
                        NotificationScreen()
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun UserHomeScreenPreview() {
    UserHomeScreen(navController = rememberNavController())
}