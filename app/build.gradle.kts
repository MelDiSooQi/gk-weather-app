import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // the keys should be in local.properties
    // to be local and avoid API Keys check-in into Version Control System.
    // i keep them only here this time for testing purposes
    // but the implementation will be here for both of them
    val localProperties =
        gradleLocalProperties(rootDir)

    val OPEN_WEATHER_API_KEY: String = localProperties.getProperty("OPEN_WEATHER_API_KEY")
    val MAPS_API_KEY: String = localProperties.getProperty("MAPS_API_KEY")

    signingConfigs {
        create("release") {
            storeFile = file("../keystore/WA-keystore-pass-123456.jks") //keystore path
            storePassword = "123456"
            keyAlias = "WA"
            keyPassword = "123456"
            // Optional, specify signing versions used
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }

    buildTypes {
        debug {
            android.buildFeatures.buildConfig = true
            applicationIdSuffix = ".debug"
            // the keys should be in local.properties
            // to be local and avoid API Keys check-in into Version Control System.
            // i keep them only here this time for testing purposes
            // but the implementation will be here for both of them
            buildConfigField("String", "API_BASE_URL", "\"https://api.openweathermap.org/data/2.5/\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"53f9d8e4213222cf517d86dc406d67fc\"")
//            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$OPEN_WEATHER_API_KEY\"")
            buildConfigField("String", "MAPS_API_KEY", "\"AIzaSyAQwOL3y96nGxWxYXi35TlHMHGmiHzdYpk\"")
//            buildConfigField("String", "MAPS_API_KEY", "\"$MAPS_API_KEY\"")
            isDebuggable = true
        }
        release {
            android.buildFeatures.buildConfig = true
            // the keys should be in local.properties
            // to be local and avoid API Keys check-in into Version Control System.
            // i keep them only here this time for testing purposes
            // but the implementation will be here for both of them
            buildConfigField("String", "API_BASE_URL", "\"https://api.openweathermap.org/data/2.5/\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"53f9d8e4213222cf517d86dc406d67fc\"")
//            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$OPEN_WEATHER_API_KEY\"")
            buildConfigField("String", "MAPS_API_KEY", "\"AIzaSyAQwOL3y96nGxWxYXi35TlHMHGmiHzdYpk\"")
//            buildConfigField("String", "MAPS_API_KEY", "\"$MAPS_API_KEY\"")
            signingConfig = signingConfigs.getByName("release")
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // UI
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Retrofit components (for API Calls)
    implementation("com.squareup.retrofit2:retrofit:2.6.4")
    implementation("com.squareup.retrofit2:converter-gson:2.6.4")

    //Draw a chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.0.3")

    // Google Services
    implementation("com.google.android.gms:play-services-location:21.0.1") // Location Service
    implementation("com.google.android.gms:play-services-maps:18.2.0") // Map Services

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}