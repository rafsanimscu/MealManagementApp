package com.mealsystem.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mealsystem.app.R
import com.mealsystem.app.data.AppDatabase // We will create this in Step 8!
import com.mealsystem.app.utils.SecurityUtils
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    
    // Database
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize DB
        db = AppDatabase.getDatabase(this)

        // Bind Views
        etEmail = findViewById(R.id.tilEmail).editText!!
        etPassword = findViewById(R.id.tilPassword).editText!!

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin).setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        // 1. Input Validation
        if (!SecurityUtils.isValidEmail(email)) {
            etEmail.error = "Invalid Email"
            return
        }
        if (password.isEmpty()) {
            etPassword.error = "Password required"
            return
        }

        // 2. Database Check (Background Thread)
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                db.mealSystemDao().login(email, SecurityUtils.hashPassword(password))
            }

            if (user != null) {
                if (!user.isActive) {
                    Toast.makeText(this@LoginActivity, "Account deactivated", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                // 3. Role Validation & Routing
                routeUser(user.roleId, user.userId)
            } else {
                Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun routeUser(roleId: Int, userId: Int) {
        // Logic to switch screens based on Role
        // Note: We haven't created these Activities' Kotlin files yet, but this sets up the logic
        
        /*
        val intent = if (roleId == 1) { // Assuming 1 is Admin
            Intent(this, AdminDashboardActivity::class.java)
        } else {
            Intent(this, UserDashboardActivity::class.java)
        }
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
        */
       
       // For now, just show a message
       val roleText = if (roleId == 1) "Admin" else "User"
       Toast.makeText(this, "Welcome $roleText!", Toast.LENGTH_LONG).show()
    }
}