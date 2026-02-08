package com.mealsystem.app.utils

import com.mealsystem.app.data.AppDatabase
import com.mealsystem.app.data.dao.MealSystemDao
import com.mealsystem.app.data.source.LocalMealDataSource
import com.mealsystem.app.data.source.MealDataSource
import com.mealsystem.app.repository.MealRepository

// SINGLETON: Holds the "Live" instance of our logic
object ServiceLocator {
    
    private var database: AppDatabase? = null
    private var repository: MealRepository? = null

    // Initialize with Context
    fun provideDatabase(context: android.content.Context): AppDatabase {
        return database ?: AppDatabase.getDatabase(context).also { database = it }
    }

    // Provide the Repository (Uses Local Data Source)
    fun provideRepository(context: android.content.Context): MealRepository {
        return repository ?: MealRepository(
            LocalMealDataSource(provideDatabase(context).mealSystemDao())
        ).also { repository = it }
    }
}