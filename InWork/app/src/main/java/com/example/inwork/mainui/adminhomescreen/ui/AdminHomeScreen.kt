package com.example.inwork.mainui.adminhomescreen.ui

// ✅ Import the new AI feature and its ViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.inwork.core.navigation.Screen
import com.example.inwork.core.utils.components.LocationPermissionBanner
import com.example.inwork.core.utils.navigationbar.AdminBottomAppBar
import com.example.inwork.core.utils.navigationbar.AdminSideBar
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.mainui.addemployeescreen.ui.AddEmployeeScreen
import com.example.inwork.mainui.addofficescreen.ui.AddOfficeScreen
import com.example.inwork.mainui.admineventscreen.ui.AddEventScreen
import com.example.inwork.mainui.adminhomescreen.viewmodel.AdminHomeEvent
import com.example.inwork.mainui.adminhomescreen.viewmodel.AdminHomeViewModel
import com.example.inwork.mainui.adminsettings.ui.AdminSettingsScreen
import com.example.inwork.mainui.aifeature.ui.DraggableAIFab
import com.example.inwork.mainui.contactusscreen.ui.ContactUsContent
import com.example.inwork.mainui.noticescreen.ui.SendNoticeScreen
import com.example.inwork.mainui.notificationscreen.NotificationScreen
import com.example.inwork.mainui.profilescreen.ui.ProfileScreen
import com.example.inwork.mainui.viewmodels.GeminiViewModel
import kotlinx.coroutines.launch

// This sealed class remains unchanged
sealed class AdminScreen(val title: String) {
    object Home : AdminScreen("Home")
    object AllMenu : AdminScreen("All Menu")
    object SentNotice : AdminScreen("Sent Notices")
    object Notification : AdminScreen("Notifications")
    object CreateNotice : AdminScreen("Notice")
    object AddEvent : AdminScreen("Add Event")
    object AddEmployee : AdminScreen("Add Employee")
    object AddOffice : AdminScreen("Add Office")
    object AllOffices : AdminScreen("All Offices")
    object Dashboard : AdminScreen("Dashboard")
    object ContactUs : AdminScreen("Contact Us")
    object Settings : AdminScreen("Settings")
    object Profile : AdminScreen("Profile")
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminHomeViewModel = hiltViewModel(),
    // ✅ Get an instance of the GeminiViewModel
    geminiViewModel: GeminiViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val currentScreen = state.currentScreen
    val context = LocalContext.current

    // All your existing handlers and permission logic remain unchanged
    BackHandler(enabled = state.screenStack.size > 1) {
        viewModel.onEvent(AdminHomeEvent.NavigateBack)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(AdminHomeEvent.CheckLocationPermission(context))
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
            viewModel.onEvent(AdminHomeEvent.LocationPermissionUpdated(isGranted))
        }
    )

    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.onEvent(AdminHomeEvent.LocationPermissionUpdated(true))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            } else {
                viewModel.onEvent(AdminHomeEvent.LocationPermissionUpdated(false))
            }
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Your AdminSideBar remains unchanged
                AdminSideBar(
                    navController = navController,
                    companyName = "Apple",
                    email = "soumadeepbarik@gmail.com",
                    closeDrawer = { scope.launch { drawerState.close() } },
                    onHomeClick = {
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(AdminScreen.Home))
                        scope.launch { drawerState.close() }
                    },
                    onProfileClick = {
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(AdminScreen.Profile))
                        scope.launch { drawerState.close() }
                    },
                    onAllEmployeesClick = { /* TODO */ },
                    onSentNoticesClick = {
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(AdminScreen.SentNotice))
                        scope.launch { drawerState.close() }
                    },
                    onCreateNoticeClick = {
                        viewModel.onEvent(AdminHomeEvent.CreateNoticeClicked)
                        scope.launch { drawerState.close() }
                    },
                    onAddEventClick = {
                        viewModel.onEvent(AdminHomeEvent.AddEventClicked)
                        scope.launch { drawerState.close() }
                    },
                    onAddEmployeeClick = {
                        viewModel.onEvent(AdminHomeEvent.AddEmployeeClicked)
                        scope.launch { drawerState.close() }
                    },
                    onAddOfficesClick = {
                        viewModel.onEvent(AdminHomeEvent.AddOfficesClicked)
                        scope.launch { drawerState.close() }
                    },
                    onAllOfficesClick = {
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(AdminScreen.AllOffices))
                        scope.launch { drawerState.close() }
                    },
                    onDashboardClick = {
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(AdminScreen.Dashboard))
                        scope.launch { drawerState.close() }
                    },
                    onLogoutClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onDeleteAccountClick = { /* TODO: Implement Delete Account */ },
                    onContactUsClick = {
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(AdminScreen.ContactUs))
                        scope.launch { drawerState.close() }
                    },
                    onSettingsClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.AdminSettingsScreen.route)
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
                AdminBottomAppBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen ->
                        viewModel.onEvent(AdminHomeEvent.ScreenSelected(newScreen))
                    }
                )
            },
            floatingActionButton = {
                // Your original FAB remains unchanged
                FloatingActionButton(
                    onClick = { viewModel.onEvent(AdminHomeEvent.CreateNoticeClicked) },
                    modifier = Modifier.offset(y = 60.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Notice",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            // ✅ Wrap the content in a Box to allow the AI FAB to float on top
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // This Column is your original screen content
                Column(modifier = Modifier.fillMaxSize()) {
                    if (!state.hasLocationPermission) {
                        LocationPermissionBanner(onBannerClick = {
                            val hasForeground = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            if (hasForeground) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                }
                            } else {
                                foregroundLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        })
                    }

                    // The when statement and all its content remain unchanged
                    when (currentScreen) {
                        is AdminScreen.Home -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Welcome to the Admin Dashboard!",
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }
                        // ... all other 'when' cases are unchanged
                        is AdminScreen.AllMenu -> {
                            AllMenuContent(modifier = Modifier.fillMaxSize(), navController = navController)
                        }
                        is AdminScreen.SentNotice -> {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)) {
                                Text(text = "Sent Notice Content - A list of sent notices would go here.")
                            }
                        }
                        is AdminScreen.CreateNotice -> {
                            SendNoticeScreen { title, notice ->
                                println("Sending Notice -> Title: $title, Notice: $notice")
                                viewModel.onEvent(AdminHomeEvent.NavigateBack)
                            }
                        }
                        is AdminScreen.AddEvent -> {
                            AddEventScreen(navController = navController)
                        }
                        is AdminScreen.AddEmployee -> {
                            AddEmployeeScreen(onNavigateBack = {
                                viewModel.onEvent(AdminHomeEvent.NavigateBack)
                            })
                        }
                        is AdminScreen.AddOffice -> {
                            AddOfficeScreen(navController = navController)
                        }
                        is AdminScreen.AllOffices -> {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)) {
                                Text(text = "All Offices Screen Content")
                            }
                        }
                        is AdminScreen.Dashboard -> {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)) {
                                Text(text = "Dashboard Screen Content")
                            }
                        }
                        is AdminScreen.Notification -> {
                            NotificationScreen()
                        }
                        is AdminScreen.ContactUs -> {
                            ContactUsContent()
                        }
                        is AdminScreen.Settings -> {
                            AdminSettingsScreen()
                        }
                        is AdminScreen.Profile -> {
                            ProfileScreen(navController = navController)
                        }
                    }
                }
                // ✅ Add the new draggable AI FAB here. It will float above all other content.
                DraggableAIFab(geminiViewModel = geminiViewModel)
            }
        }
    }
}