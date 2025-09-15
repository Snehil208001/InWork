package com.example.inwork.mainui.adminhomescreen.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.utils.components.LocationPermissionBanner
import com.example.inwork.core.utils.navigationbar.AdminBottomAppBar
import com.example.inwork.core.utils.navigationbar.AdminSideBar
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.mainui.notificationscreen.NotificationScreen
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf<AdminScreen>(AdminScreen.Home) }
    val context = LocalContext.current

    // --- Start of Banner Logic ---

    // State to track if location permission is granted
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Observe lifecycle events to refresh permission status on resume
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasLocationPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Launcher for background location permission
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                hasLocationPermission = true
            }
        }
    )

    // Launcher for foreground location permission
    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                hasLocationPermission = true
                // Now that we have foreground permission, we can request background permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
        }
    )


    // --- End of Banner Logic ---


    // Launcher for notification permission
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // You can handle the result here if needed
        }
    )

    // Request notification permission when the screen is first displayed
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
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
                AdminBottomAppBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen -> currentScreen = newScreen }
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Show banner if permission is not granted
                if (!hasLocationPermission) {
                    LocationPermissionBanner(onBannerClick = {
                        val hasForegroundPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasForegroundPermission) {
                            // If we have foreground, request background
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            }
                        } else {
                            // If we don't have foreground, request it first
                            foregroundLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    })
                }

                // Conditionally display content based on the currentScreen state
                when (currentScreen) {
                    is AdminScreen.Home -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            Text(text = "Admin Home Screen Content")
                        }
                    }

                    is AdminScreen.AllMenu -> {
                        AllMenuContent(modifier = Modifier.fillMaxSize(), navController = navController)
                    }

                    is AdminScreen.SentNotice -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            Text(text = "Sent Notice Content")
                        }
                    }

                    is AdminScreen.Notification -> {
                        NotificationScreen()
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