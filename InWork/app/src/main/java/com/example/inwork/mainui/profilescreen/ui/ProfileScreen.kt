package com.example.inwork.mainui.profilescreen.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.inwork.R
import com.example.inwork.mainui.profilescreen.viewmodel.ProfileViewModel
import com.example.inwork.ui.theme.CameraFabColor

// Define Colors to match the UI
val ProfileGreen = Color(0xFFC8E6C9)
val LightGreyBackground = Color(0xFFF5F5F5)

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    // Your existing logic - Unchanged
    val profileImageUri by profileViewModel.profileImageUri
    val name by profileViewModel.name
    val email by profileViewModel.email
    val companyId by profileViewModel.companyId
    val department by profileViewModel.department
    val designation by profileViewModel.designation
    val phone by profileViewModel.phone
    val context = LocalContext.current

    val imageCropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            profileViewModel.onProfileImageChange(result.uriContent)
        } else {
            val exception = result.error
            Toast.makeText(context, "Image Cropping failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreyBackground)
            .statusBarsPadding() // The fix is applied here
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ProfileGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Image(
                        painter = if (profileImageUri != null) {
                            rememberAsyncImagePainter(model = profileImageUri)
                        } else {
                            painterResource(id = R.drawable.logopreview) // Fallback icon
                        },
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable {
                                val cropOptions = CropImageContractOptions(null, CropImageOptions())
                                imageCropLauncher.launch(cropOptions)
                            },
                        contentScale = ContentScale.Crop
                    )
                    FloatingActionButton(
                        onClick = {
                            val cropOptions = CropImageContractOptions(null, CropImageOptions())
                            imageCropLauncher.launch(cropOptions)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp),
                        containerColor = CameraFabColor,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = "Change Picture",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = name.ifEmpty { "Soumadeep" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = designation.ifEmpty { "Managing Director" },
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Details Cards - using your ViewModel state
        ProfileInfoCard(
            icon = Icons.Default.Business,
            title = "Company ID",
            value = companyId.ifEmpty { "souma123" })
        ProfileInfoCard(
            icon = Icons.Default.Apartment,
            title = "Company Name",
            value = "Apple"
        ) // Placeholder as it's not in ViewModel
        ProfileInfoCard(
            icon = Icons.Default.Settings,
            title = "Industry",
            value = department.ifEmpty { "IT" })
        ProfileInfoCard(
            icon = Icons.Default.Phone,
            title = "Mobile Number",
            value = phone.ifEmpty { "1234567890" })
        ProfileInfoCard(
            icon = Icons.Default.Email,
            title = "Email",
            value = email.ifEmpty { "soumadeepbarik@gmail.com" })

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = ProfileGreen),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Logout", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            TextButton(
                onClick = { /* Handle update password */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Update Password?", color = Color.Gray)
            }
        }
    }
}

@Composable
fun ProfileInfoCard(icon: ImageVector, title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.Black)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(text = value, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val dummyNavController = rememberNavController()
    ProfileScreen(navController = dummyNavController)
}