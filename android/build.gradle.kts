plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
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
