# Keep Room Database Entities
-keep class com.mealsystem.app.data.entity.** { *; }
-keep class com.mealsystem.app.data.dao.** { *; }
-keep class com.mealsystem.app.data.database.** { *; }

# Keep Gson (if used later for JSON)
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**

# Keep ViewModels (if used)
-keep class com.mealsystem.app.viewmodel.** { *; }