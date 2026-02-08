    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_system_database"
                ).build()
                
                // Run the seed logic in the background
                CoroutineScope(Dispatchers.IO).launch {
                    // UPGRADE: Use TestUtils instead of manual seeding
                    TestUtils.seedTestData(instance.mealSystemDao())
                }
                
                INSTANCE = instance
                instance
            }
        }
        
        // OLD seedDatabase function is no longer needed and can be deleted
        // (Or you can keep it empty, but TestUtils handles it now)
    }