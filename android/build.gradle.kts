plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.9.0")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.gomoku.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.gomoku.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
