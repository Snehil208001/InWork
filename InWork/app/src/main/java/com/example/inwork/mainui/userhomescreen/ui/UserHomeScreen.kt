package com.example.inwork.mainui.userhomescreen.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.utils.components.LocationPermissionBanner
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.core.utils.navigationbar.UserBottomAppBar
import com.example.inwork.core.utils.navigationbar.UserSideBar
import com.example.inwork.mainui.geoscreen.ui.AddGeoScreen
import com.example.inwork.mainui.notificationscreen.NotificationScreen
import com.example.inwork.mainui.userhomescreen.viewmodel.UserHomeEvent
import com.example.inwork.mainui.userhomescreen.viewmodel.UserHomeViewModel
import kotlinx.coroutines.launch

sealed class UserScreen(val title: String) {
    object Home : UserScreen("Home")
    object AddGeo : UserScreen("Add Geo")
    object Notices : UserScreen("Notices")
    object Notification : UserScreen("Notifications")
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun UserHomeScreen(
    navController: NavController,
    viewModel: UserHomeViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val currentScreen = state.currentScreen
    val context = LocalContext.current
    var showSosDialog by remember { mutableStateOf(false) }
    var sosText by remember { mutableStateOf("Emergency Situation") }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (!spokenText.isNullOrEmpty()) {
                sosText = spokenText
            }
        }
    }

    if (showSosDialog) {
        SosDialog(
            sosText = sosText,
            onTextChange = { newText -> sosText = newText },
            onDismiss = { showSosDialog = false },
            onSend = { message ->
                println("SOS Sent: $message") // Placeholder for send logic
                showSosDialog = false
            },
            onMicClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
                }
                try {
                    speechRecognizerLauncher.launch(intent)
                } catch (e: Exception) {
                    // Handle case where speech recognition is not supported
                    println("Speech recognition not supported: ${e.message}")
                }
            }
        )
    }

    BackHandler(enabled = state.screenStack.size > 1) {
        viewModel.onEvent(UserHomeEvent.NavigateBack)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(UserHomeEvent.CheckLocationPermission(context))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onEvent(UserHomeEvent.LocationPermissionUpdated(isGranted))
        }
    )

    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.onEvent(UserHomeEvent.LocationPermissionUpdated(true))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            } else {
                viewModel.onEvent(UserHomeEvent.LocationPermissionUpdated(false))
            }
        }
    )

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
                    onClick = { showSosDialog = true },
                    modifier = Modifier.offset(y = 60.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFF00FF66),
                    contentColor = Color.Black,
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
                if (!state.hasLocationPermission) {
                    LocationPermissionBanner(onBannerClick = {
                        val hasForegroundPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasForegroundPermission) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            }
                        } else {
                            foregroundLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    })
                }

                when (currentScreen) {
                    is UserScreen.Home -> {
                        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                            Text(text = "User Home Screen Content")
                        }
                    }
                    is UserScreen.AddGeo -> {
                        AddGeoScreen()
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

@Composable
fun SosDialog(
    sosText: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSend: (String) -> Unit,
    onMicClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Enter SOS Text", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = sosText,
                onValueChange = onTextChange,
                label = { Text("Enter the SOS text..") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = onMicClick) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = Color.White,
                            modifier = Modifier
                                .background(Color(0xFF4CAF50), CircleShape)
                                .padding(8.dp)
                        )
                    }
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSend(sosText)
                }
            ) {
                Text("SEND")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview(showSystemUi = true)
@Composable
fun UserHomeScreenPreview() {
    UserHomeScreen(navController = rememberNavController())
}