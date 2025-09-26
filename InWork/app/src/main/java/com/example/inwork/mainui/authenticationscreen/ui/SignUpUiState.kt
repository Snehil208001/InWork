package com.example.inwork.mainui.authenticationscreen.ui

// This data class holds all the information and error states for the sign-up form.
data class SignUpUiState(
    val companyId: String = "",
    val companyName: String = "",
    val industry: String = "",
    val managingDirector: String = "",
    val mobile: String = "",
    val email: String = "",
    val city: String = "",
    val companyIdError: String? = null,
    val companyNameError: String? = null,
    val industryError: String? = null,
    val managingDirectorError: String? = null,
    val mobileError: String? = null,
    val emailError: String? = null,
    val cityError: String? = null,
    val signUpSuccessful: Boolean = false
)