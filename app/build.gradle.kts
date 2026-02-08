plugins { id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
 id("com.android.application"); id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
   }
android {
    namespace = "com.mealsystem.app"
    compileSdk = 34
    defaultConfig { applicationId = "com.mealsystem.app"; minSdk = 26; targetSdk = 34; versionCode = 1; versionName = "1.0" }
    buildTypes { release { isMinifyEnabled = false } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // --- Room Database Dependencies ---
    // Room
val roomVersion = "2.6.1"
implementation("androidx.room:room-runtime:$roomVersion")
implementation("androidx.room:room-ktx:$roomVersion")
kapt("androidx.room:room-compiler:$roomVersion")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Kotlin Extensions
    ksp("androidx.room:room-compiler:$roomVersion") // Annotation Processor
    
    // --- Coroutines (For Database Operations) ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}