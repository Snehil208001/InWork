package com.example.inwork.mainui.authenticationscreen.viewodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inwork.mainui.authenticationscreen.ui.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Placeholder functions (Assume these are imported from ValidationUtils.kt)
// You MUST replace these with your actual validation logic.
fun isValidProfessionalEmail(email: String): Pair<Boolean, String?> = Pair(true, null)
fun isValidMobileNumber(mobile: String): Boolean = mobile.length == 10 // Basic placeholder check

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
        // Note: Replace the placeholder validation functions with your actual imported ones.
        val emailValidation = isValidProfessionalEmail(currentState.email)

        val companyIdError = if (currentState.companyId.isBlank()) "Company ID cannot be empty" else null
        val companyNameError = if (currentState.companyName.isBlank()) "Company Name cannot be empty" else null
        val industryError = if (currentState.industry.isBlank()) "Industry cannot be empty" else null
        val directorError = if (currentState.managingDirector.isBlank()) "Director name cannot be empty" else null
        val cityError = if (currentState.city.isBlank()) "City cannot be empty" else null
        val mobileError = if (currentState.mobile.isBlank()) "Mobile cannot be empty" else if (!isValidMobileNumber(currentState.mobile)) "Enter a valid 10-digit mobile number" else null
        val emailError = if (currentState.email.isBlank()) "Email cannot be empty" else emailValidation.second

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