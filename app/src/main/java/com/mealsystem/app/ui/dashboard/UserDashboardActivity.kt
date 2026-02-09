package com.mealsystem.app.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mealsystem.app.R
import com.mealsystem.app.data.AppDatabase
import com.mealsystem.app.data.entity.MealLogEntity
import com.mealsystem.app.data.entity.SettingsEntity
import com.mealsystem.app.repository.MealRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserDashboardActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var repository: MealRepository
    private var userId = -1
    private var settings: SettingsEntity? = null
    private var mealsToday = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        // 1. Get User ID passed from Login
        userId = intent.getIntExtra("USER_ID", -1)

        // 2. Initialize DB
        db = AppDatabase.getDatabase(this)
        
        // 3. Initialize Repository
        // Note: In a real app, use ServiceLocator. For simplicity here we create direct instance
        repository = MealRepository(db.mealSystemDao())

        // 4. Setup Views
        setupViews()
        
        // 5. Load Data
        loadData()
    }

    private fun setupViews() {
        // Meal Buttons
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBreakfast).setOnClickListener { addMeal("Breakfast") }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLunch).setOnClickListener { addMeal("Lunch") }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDinner).setOnClickListener { addMeal("Dinner") }
        
        // History Button
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnViewHistory).setOnClickListener {
            showHistoryDialog()
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            // Load Settings
            settings = db.mealSystemDao().getSettings()
            updateBalanceDisplay()
        }
    }

    private fun addMeal(type: String) {
        if (settings == null) {
            Toast.makeText(this, "System not ready. Try again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check Month Lock
        val activeMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())
        
        lifecycleScope.launch {
            val meal = MealLogEntity(
                userId = userId,
                date = System.currentTimeMillis(), // Timestamp
                mealType = type,
                guestCount = 0,
                appliedMealRate = settings!!.costPerMeal,
                timestamp = System.currentTimeMillis()
            )

            val success = repository.addMealWithLock(meal, activeMonth)
            
            if (success) {
                mealsToday++
                Toast.makeText(this, "$type Logged!", Toast.LENGTH_SHORT).show()
                updateBalanceDisplay()
            } else {
                Toast.makeText(this, "Month Locked or Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBalanceDisplay() {
        // Visualization: Calculate approximate balance based on Payments vs (Meals * Rate)
        // For now, we just show the text.
        // A full calculation requires aggregating all payments, which we'll do in UI next steps.
        
        val balanceText = findViewById<android.widget.TextView>(R.id.tvBalanceAmount)
        // Placeholder logic: In next detour we fetch real balance
        balanceText.text = "Updating..." 
    }

    private fun showHistoryDialog() {
        // Visualization: Show recent meals in a list
        // For now, using a simple AlertDialog text
        lifecycleScope.launch {
            val meals = db.mealSystemDao().getMealLogsByUser(userId)
            
            val sb = StringBuilder()
            sb.append("Recent Meals:\n\n")
            meals.take(5).forEach {
                val date = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(it.date))
                sb.append("$date - ${it.mealType} (${it.appliedMealRate})\n")
            }
            
            AlertDialog.Builder(this)
                .setTitle("History")
                .setMessage(sb.toString())
                .setPositiveButton("OK", null)
                .show()
        }
    }
}