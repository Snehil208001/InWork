package com.example.inwork.mainui.authenticationscreen.viewodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inwork.mainui.authenticationscreen.ui.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Placeholder function - replace with your actual validation logic if needed.
fun isValidMobileNumber(mobile: String): Boolean = mobile.length == 10 && mobile.all { it.isDigit() }

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onCompanyIdChange(value: String) = _uiState.update { it.copy(companyId = value, companyIdError = null) }
    fun onCompanyNameChange(value: String) = _uiState.update { it.copy(companyName = value, companyNameError = null) }
    fun onIndustryChange(value: String) = _uiState.update { it.copy(industry = value, industryError = null) }
    fun onDirectorChange(value: String) = _uiState.update { it.copy(managingDirector = value, managingDirectorError = null) }
    fun onMobileChange(value: String) = _uiState.update { it.copy(mobile = value, mobileError = null) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, emailError = null) }
    fun onCityChange(value: String) = _uiState.update { it.copy(city = value, cityError = null) }

    fun onAddAdminClick() {
        val currentState = _uiState.value

        val companyIdError = if (currentState.companyId.isBlank()) {
            "Company ID cannot be empty"
        } else if (!currentState.companyId.all { it.isLetterOrDigit() }) {
            "Company ID can only contain letters and numbers"
        } else {
            null
        }

        val companyNameError = if (currentState.companyName.isBlank()) {
            "Company Name cannot be empty"
        } else if (!currentState.companyName.all { it.isLetter() || it.isWhitespace() }) {
            "Company Name can only contain letters"
        } else {
            null
        }

        val industryError = if (currentState.industry.isBlank()) {
            "Industry cannot be empty"
        } else if (!currentState.industry.all { it.isLetter() || it.isWhitespace() }) {
            "Industry can only contain letters"
        } else {
            null
        }

        val directorError = if (currentState.managingDirector.isBlank()) {
            "Director name cannot be empty"
        } else if (!currentState.managingDirector.all { it.isLetter() || it.isWhitespace() }) {
            "Director name can only contain letters"
        } else {
            null
        }

        val cityError = if (currentState.city.isBlank()) {
            "City cannot be empty"
        } else if (!currentState.city.all { it.isLetter() || it.isWhitespace() }) {
            "City can only contain letters"
        } else {
            null
        }

        val mobileError = if (currentState.mobile.isBlank()) {
            "Mobile cannot be empty"
        } else if (!isValidMobileNumber(currentState.mobile)) {
            "Enter a valid 10-digit mobile number"
        } else {
            null
        }

        val emailError = if (currentState.email.isBlank()) {
            "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            "Enter a valid email address"
        } else {
            null
        }

        val hasError = listOfNotNull(
            companyIdError, companyNameError, industryError, directorError, cityError, mobileError, emailError
        ).isNotEmpty()

        if (hasError) {
            _uiState.update {
                it.copy(
                    companyIdError = companyIdError,
                    companyNameError = companyNameError,
                    industryError = industryError,
                    managingDirectorError = directorError,
                    cityError = cityError,
                    mobileError = mobileError,
                    emailError = emailError
                )
            }
            return
        }
        viewModelScope.launch {
            println("All fields are valid! Proceeding with signup.")
            // Simulate a successful API call or operation
            _uiState.update { it.copy(signUpSuccessful = true) }
        }
    }
}