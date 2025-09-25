package com.example.inwork.mainui.imageuploadscreen.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.inwork.R
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import com.example.inwork.mainui.imageuploadscreen.viewmodel.ImageUploadViewModel
import com.example.inwork.mainui.imageuploadscreen.viewmodel.UploadState

@Composable
fun ImageUploadScreen(
    navController: NavController,
    viewModel: ImageUploadViewModel = viewModel()
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val uploadState by viewModel.uploadState.collectAsState()

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
        } else {
            val exception = result.error
            Toast.makeText(context, "Image Cropping failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val cropOptions = CropImageContractOptions(null, CropImageOptions())
            imageCropLauncher.launch(cropOptions)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uploadState) {
        when (val state = uploadState) {
            is UploadState.Success -> {
                Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_LONG).show()
                viewModel.resetState()
                navController.popBackStack()
            }
            is UploadState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            InWorkTopAppBar(
                title = "Image Upload",
                onNavigationIconClick = { navController.popBackStack() }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFE6F5E6))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF00B300))
                        .clickable {
                            when (ContextCompat.checkSelfPermission(context, permissionToRequest)) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    val cropOptions = CropImageContractOptions(null, CropImageOptions())
                                    imageCropLauncher.launch(cropOptions)
                                }
                                else -> {
                                    permissionLauncher.launch(permissionToRequest)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logopreview),
                            contentDescription = "Logo",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.uploadImage(context, imageUri) },
                    enabled = uploadState !is UploadState.Uploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B300))
                ) {
                    if (uploadState is UploadState.Uploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(text = "Upload", color = Color.White, fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3E6B3))
                ) {
                    Text(text = "Back", color = Color.Black, fontSize = 18.sp)
                }
            }
        }
    )
}