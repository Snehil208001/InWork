package com.example.inwork.mainui.contactusscreen.viewodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

// Sealed class to represent the different user interactions on this screen
sealed class ContactUsEvent {
    data object OnEmailClicked : ContactUsEvent()
    data object OnCallClicked : ContactUsEvent()
}

class ContactUsViewModel : ViewModel() {

    fun onEvent(event: ContactUsEvent, context: Context) {
        when (event) {
            is ContactUsEvent.OnEmailClicked -> {
                sendEmail(context)
            }
            is ContactUsEvent.OnCallClicked -> {
                dialPhoneNumber(context)
            }
        }
    }

    private fun sendEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf("invyusolutions@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "InWork App Inquiry")
        }
        // Check if there's an app that can handle this intent before starting it
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    private fun dialPhoneNumber(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+918002256140")
        }
        // Check if there's an app that can handle this intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}