plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt") // Essential for Room Database
}

android {
    namespace = "com.mealsystem.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mealsystem.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    // 2. SIGNING CONFIGS (Must be defined BEFORE buildTypes)
    signingConfigs {
        create("release") {
            // Using default debug keystore for demonstration to ensure build succeeds without manual file upload
            storeFile = file(System.getProperty("user.home") + "/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    // 3. BUILD TYPES (References signingConfigs)
    buildTypes {
        release {
            isMinifyEnabled = true // Shrinks the APK
            isShrinkResources = true // Removes unused images
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Important: Sign the release APK
            signingConfig = signingConfigs.getByName("release")
        }
        
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // --- ROOM DATABASE ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    
    // --- COROUTINES ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}