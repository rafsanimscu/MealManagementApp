package com.mealsystem.app.utils

import java.security.MessageDigest

object SecurityUtils {
    
    // Hashes a password using SHA-256
    fun hashPassword(password: String): String {
        if (password.isEmpty()) return ""
        
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Simple Input Validation (Crash Prevention)
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}