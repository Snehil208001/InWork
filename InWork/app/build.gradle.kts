plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.inwork"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.inwork"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ This loads the API key from local.properties
        manifestPlaceholders["AIzaSyBHxR1SKv5GpYGcixvLPifItUuO-zk476k"] =
            project.findProperty("AIzaSyBHxR1SKv5GpYGcixvLPifItUuO-zk476k") as String? ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val lottieVersion = "6.3.0" // Replace with the latest stable version
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    val nav_version = "2.9.3"

    implementation("androidx.navigation:navigation-compose:$nav_version")


    // Standard Android dependencies
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose Bill of Materials (BOM) - Manages versions for other Compose libraries
    // Make sure to use the latest stable version
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))

    // Core Compose UI libraries (included by the BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material Design 3 components (Buttons, Cards, TextFields, etc.)
    implementation("androidx.compose.material3:material3")

    // Icons library (for the password visibility icon)
    implementation("androidx.compose.material:material-icons-extended")


    // Android Studio tooling support (for previews and inspection)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    // Add this line for hiltViewModel()
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Google Maps for Compose
    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    // Coil for Image Loading (using v2 for stability with these dependencies)
    implementation("io.coil-kt:coil-compose:2.4.0")

    //cropper
    implementation("com.vanniktech:android-image-cropper:4.5.0")
}