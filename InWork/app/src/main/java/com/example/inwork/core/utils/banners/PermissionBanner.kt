package com.example.inwork.core.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A banner to inform the user that location permission is disabled.
 * Clicking it should take the user to the app settings.
 *
 * @param onBannerClick The action to perform when the banner is clicked.
 */
@Composable
fun LocationPermissionBanner(
    onBannerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant) // Using a subtle background color from your theme
            .clickable { onBannerClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOff,
                contentDescription = "Location Disabled",
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Darker tint for icon
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Location sharing disabled. Tap here to enable.",
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Darker text for readability
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Go to settings",
            tint = MaterialTheme.colorScheme.onSurfaceVariant // Darker tint for icon
        )
    }
}