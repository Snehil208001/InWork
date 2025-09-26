package com.example.inwork.mainui.userhomescreen.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.inwork.core.navigation.Screen
import com.example.inwork.core.utils.components.LocationPermissionBanner
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.core.utils.navigationbar.UserBottomAppBar
import com.example.inwork.core.utils.navigationbar.UserSideBar
import com.example.inwork.mainui.contactusscreen.ui.ContactUsContent
import com.example.inwork.mainui.eventscreen.ui.CheckEventScreen
import com.example.inwork.mainui.imageuploadscreen.ui.ImageUploadScreen
import com.example.inwork.mainui.leavescreen.ui.PostLeaveScreen
import com.example.inwork.mainui.notificationscreen.NotificationScreen
import com.example.inwork.mainui.profilescreen.ui.ProfileScreen
import com.example.inwork.mainui.userhomescreen.viewmodel.UserHomeEvent
import com.example.inwork.mainui.userhomescreen.viewmodel.UserHomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

// --- NEW IMPORTS ---
import android.location.Geocoder
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
// -------------------


sealed class UserScreen(val title: String) {
    object Home : UserScreen("Home")
    object AddGeo : UserScreen("Add Geo")
    object Notices : UserScreen("Notices")
    object Notification : UserScreen("Notifications")
    object PostLeave : UserScreen("Post Leave")
    object CheckEvent : UserScreen("Check Event")
    object ContactUs : UserScreen("Contact Us")
    object Profile : UserScreen("Profile")
    object ImageUpload : UserScreen("Upload Photo")
}

// --- NEW UTILITY FUNCTION TO RESOLVE ADDRESS (GEOCODING) ---
private fun resolveAddress(context: Context, location: Location, onAddressResolved: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Asynchronous Geocoding for API 33+
            geocoder.getFromLocation(location.latitude, location.longitude, 1) { list ->
                val address = list.firstOrNull()?.getAddressLine(0) ?: "Address not found"
                onAddressResolved(address)
            }
        } else {
            // Synchronous Geocoding for older APIs (Must be run off the main thread/coroutine scope)
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Address not found"
            onAddressResolved(address)
        }
    } catch (e: Exception) {
        onAddressResolved("Address lookup failed: ${e.message ?: "Unknown error"}")
    }
}
// --- END NEW UTILITY FUNCTION ---


// --- MODIFIED UTILITY FUNCTION FOR LAST KNOWN LOCATION ---
private fun getLastKnownLocation(context: Context, onLocationFetched: (Location?) -> Unit) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLocationFetched(location)
            }
            .addOnFailureListener {
                onLocationFetched(null)
            }
    } else {
        onLocationFetched(null)
    }
}
// --- END MODIFIED UTILITY FUNCTION ---


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
                println("SOS Sent: $message")
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
                    println("Speech recognition not supported: ${e.message}")
                }
            }
        )
    }

    // MODIFIED: Use screen stack size for BackHandler
    BackHandler(enabled = drawerState.isOpen || state.screenStack.size > 1) {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        } else {
            viewModel.onEvent(UserHomeEvent.NavigateBack)
        }
    }

    // --- NEW/MODIFIED: Location Fetching and Geocoding Logic ---
    LaunchedEffect(state.hasLocationPermission, state.currentScreen, state.currentAddress) {
        // Only fetch/refresh if we are on the Home screen and have permission
        if (state.hasLocationPermission && state.currentScreen == UserScreen.Home) {
            // Trigger fetch if location hasn't been set (null) or if a Refresh was requested (address is "Updating...")
            if (state.currentLatitude == null || state.currentAddress == "Updating...") {
                // Run location fetching/geocoding in a background context (Dispatchers.IO)
                withContext(Dispatchers.IO) {
                    getLastKnownLocation(context) { location ->
                        location?.let { loc ->
                            viewModel.onEvent(UserHomeEvent.LocationFetched(loc))
                            resolveAddress(context, loc) { address ->
                                viewModel.onEvent(UserHomeEvent.AddressResolved(address))
                            }
                        }
                    }
                }
            }
        }
    }
    // --- END NEW/MODIFIED LOGIC ---


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

    val isAddGeoScreen = currentScreen == UserScreen.AddGeo

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isAddGeoScreen,
        drawerContent = {
            ModalDrawerSheet {
                UserSideBar(
                    userName = "Ratan",
                    email = "soumadeepbarik2001@gmail.com",
                    onAddGeoClick = {
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.AddGeo))
                        scope.launch { drawerState.close() }
                    },
                    onPostLeaveClick = {
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.PostLeave))
                        scope.launch { drawerState.close() }
                    },
                    onCheckEventClick = {
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.CheckEvent))
                        scope.launch { drawerState.close() }
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.GrantPermissionsScreen.route)
                        scope.launch { drawerState.close() }
                    },
                    onContactUsClick = {
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.ContactUs))
                        scope.launch { drawerState.close() }
                    },
                    onLogoutClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.userHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onProfileClick = {
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.Profile))
                        scope.launch { drawerState.close() }
                    },
                    onUploadPhotoClick = {
                        viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.ImageUpload))
                        scope.launch { drawerState.close() }
                    }
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
                        // --- MODIFIED: Pass dynamic location data and Refresh click handler ---
                        EmployeeHomeScreen(
                            currentLatitude = state.currentLatitude,
                            currentLongitude = state.currentLongitude,
                            currentAddress = state.currentAddress,
                            onShowOnMapClick = {
                                viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.AddGeo))
                            },
                            onRefreshClick = {
                                viewModel.onEvent(UserHomeEvent.RefreshLocation)
                            }
                        )
                        // --- END MODIFIED ---
                    }
                    is UserScreen.AddGeo -> {
                        AddGeoScreen(
                            hasLocationPermission = state.hasLocationPermission,
                            isDrawerOpen = drawerState.isOpen,
                            onCloseDrawer = {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                    is UserScreen.Notices -> {
                        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                            Text(text = "Notices Screen Content")
                        }
                    }
                    is UserScreen.Notification -> {
                        NotificationScreen()
                    }
                    is UserScreen.PostLeave -> {
                        PostLeaveScreen(onPostLeave = { designation, fromDate, toDate, reason ->
                            println("Leave Request Submitted: Designation=$designation, From=$fromDate, To=$toDate, Reason=$reason")
                            viewModel.onEvent(UserHomeEvent.ScreenSelected(UserScreen.Home))
                        })
                    }
                    is UserScreen.CheckEvent -> {
                        CheckEventScreen()
                    }
                    is UserScreen.ContactUs -> {
                        ContactUsContent()
                    }
                    is UserScreen.Profile -> {
                        ProfileScreen(navController = navController)
                    }
                    is UserScreen.ImageUpload -> {
                        ImageUploadScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun AddGeoScreen(
    hasLocationPermission: Boolean,
    isDrawerOpen: Boolean,
    onCloseDrawer: () -> Unit
) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    val patna = LatLng(25.5941, 85.1376)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(patna, 10f)
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            getLastKnownLocation(context) { location ->
                currentLocation = location
                location?.let {
                    val newLatLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(newLatLng, 15f)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = hasLocationPermission)
        )

        if (isDrawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onCloseDrawer() }
                        )
                    }
            )
        }

        if (currentLocation == null && hasLocationPermission) {
            Text("Fetching current location...")
        }
    }
}

// The getLastKnownLocation utility function is defined above the UserHomeScreen composable.

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