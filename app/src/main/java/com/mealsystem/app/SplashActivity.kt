package com.mealsystem.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Using the "Hello" layout temporarily

        // UPGRADE: Auto-navigate to Login after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // We will create LoginActivity in next steps, keep this logic ready
            // val intent = Intent(this, LoginActivity::class.java)
            // startActivity(intent)
            finish()
        }, 2000)
    }
}