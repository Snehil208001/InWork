package com.example.inwork.core.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator(private val context: Context) {

    private val biometricManager = BiometricManager.from(context)

    fun authenticate(onSuccess: () -> Unit) {
        val activity = context as? FragmentActivity
        if (activity == null) {
            Log.e("BiometricAuth", "Context is not a FragmentActivity")
            return
        }

        // This allows both strong biometrics (Face ID, Fingerprint) and device credentials (PIN, Pattern, Password)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL

        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(context)
                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Called when authentication is successful with either biometrics or device credentials
                            onSuccess()
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            // Handle errors. The user canceling the screen lock prompt will also land here.
                            Log.e(
                                "BiometricAuth",
                                "Authentication error: Code $errorCode - $errString"
                            )
                            Toast.makeText(
                                context,
                                "Authentication error: $errString",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            // Called when a biometric is presented but not recognized
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                )

                // The prompt configuration
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric check for Attendance")
                    .setSubtitle("Log in using your biometric credential")
                    .setAllowedAuthenticators(authenticators)
                    // DO NOT add .setNegativeButtonText("Cancel") here.
                    // When using DEVICE_CREDENTIAL, the system provides the fallback option.
                    // Adding a negative button overrides this behavior.
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    context,
                    "No biometric or screen lock features available on this device.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    context,
                    "Biometric or screen lock features are currently unavailable.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // This is key: it now correctly informs the user they need to set up a screen lock OR biometrics.
                Toast.makeText(
                    context,
                    "You haven't set up any biometric credentials or a screen lock.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                Toast.makeText(context, "An unknown error occurred.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}