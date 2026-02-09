package com.mealsystem.app.ui.dashboard

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mealsystem.app.R
import com.mealsystem.app.data.AppDatabase
import com.mealsystem.app.data.entity.PurchaseEntity
import com.mealsystem.app.repository.MealRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var repository: MealRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        db = AppDatabase.getDatabase(this)
        repository = MealRepository(db.mealSystemDao())

        // 1. Expenses Click
        findViewById<androidx.cardview.widget.CardView>(R.id.cardExpenses).setOnClickListener {
            showAddExpenseDialog()
        }
        
        // 2. Reports Click
        findViewById<androidx.cardview.widget.CardView>(R.id.cardReports).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Reports")
                .setMessage("Generate CSV Report? (Feature coming in Upgrade 4)")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun showAddExpenseDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Kitchen Expense")

        val input = EditText(this)
        input.hint = "Description (e.g. Rice)"
        
        val inputCost = EditText(this)
        inputCost.hint = "Cost ($)"
        inputCost.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)
        layout.addView(input)
        layout.addView(inputCost)

        builder.setView(layout)

        builder.setPositiveButton("Add") { _, _ ->
            val desc = input.text.toString()
            val costStr = inputCost.text.toString()
            
            if (desc.isNotEmpty() && costStr.isNotEmpty()) {
                addExpenseToDb(desc, costStr.toDouble())
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun addExpenseToDb(desc: String, cost: Double) {
        lifecycleScope.launch {
            val expense = PurchaseEntity(
                itemName = desc,
                quantity = 1.0, // Assume 1 unit for now
                totalCost = cost,
                date = System.currentTimeMillis(),
                addedBy = 1 // Admin ID
            )
            
            // Use Repository logic (Check Lock)
            val activeMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())
            val success = repository.addPurchaseWithLock(expense, activeMonth)
            
            if (success) {
                android.widget.Toast.makeText(this, "Expense Added!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}