@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.mycontacts"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mycontacts"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // SharedPreference library
    implementation (libs.androidx.preference.ktx)

    // Image loader
    implementation(libs.glide)

    // lifecycle libraries
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Coroutines library
    implementation(libs.kotlinx.coroutines.android)

    // Firebase libraries
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation(libs.analytics.ktx)
    implementation(libs.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation (libs.firebase.auth)

}
