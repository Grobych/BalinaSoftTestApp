plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

}

android {
    namespace = "com.globa.balinasofttestapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.globa.balinasofttestapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(path = ":data:location"))
    implementation(project(path = ":data:photos"))
    implementation(project(path = ":data:login")) //TODO: remove?
    implementation(project(path = ":feature:login"))
    implementation(project(path = ":feature:photos"))
    implementation(project(path = ":feature:camera"))
    implementation(project(path = ":feature:map"))
    implementation(project(path = ":feature:photodetails"))
    implementation(project(path = ":common"))

    implementation(libs.accompanist)

    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation (libs.lifecycle.viewmodel)
    implementation(libs.bundles.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.material3)

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}