package com.example.inwork.mainui.authenticationscreen.ui

/**
 * Validates that a password meets the required complexity criteria.
 */
fun isValidPassword(password: String): Pair<Boolean, String?> {
    if (password.length < 8) {
        return Pair(false, "Password must be at least 8 characters.")
    }
    if (!password.any { it.isDigit() }) {
        return Pair(false, "Password must contain at least one digit.")
    }
    if (!password.any { it.isUpperCase() }) {
        return Pair(false, "Password must contain at least one uppercase letter.")
    }
    if (!password.any { it.isLowerCase() }) {
        return Pair(false, "Password must contain at least one lowercase letter.")
    }
    if (!password.any { !it.isLetterOrDigit() }) {
        return Pair(false, "Password must contain at least one special character.")
    }
    return Pair(true, null)
}

/**
 * Validates that an email has a correct format and belongs to a major provider.
 */
fun isValidProfessionalEmail(email: String): Pair<Boolean, String?> {
    val emailRegex = Regex("^[A-Za-z0-9](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    if (!emailRegex.matches(email)) {
        return Pair(false, "Invalid email format.")
    }
    val allowedDomains = setOf(
        "gmail.com", "yahoo.com", "hotmail.com", "aol.com", "outlook.com", "icloud.com", "protonmail.com"
    )
    val domain = email.substringAfter('@', "")
    if (domain.isBlank() || domain !in allowedDomains) {
        return Pair(false, "Enter Valid Email.")
    }
    return Pair(true, null)
}

/**
 * Validates that a mobile number is exactly 10 digits.
 */
fun isValidMobileNumber(mobile: String): Boolean {
    return mobile.length == 10 && mobile.all { it.isDigit() }
}