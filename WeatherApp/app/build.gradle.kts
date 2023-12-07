import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
    // but implementation will be here for both of them
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
            // but implementation will be here for both of them
            buildConfigField("String", "API_BASE_URL", "\"https://api.openweathermap.org/data/2.5/\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"78e0c4ae0e0a6e63c7a1b82f34c7f8f2\"")
//            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$OPEN_WEATHER_API_KEY\"")
            buildConfigField("String", "MAPS_API_KEY", "\"AIzaSyAQwOL3y96nGxWxYXi35TlHMHGmiHzdYpk\"")
//            buildConfigField("String", "MAPS_API_KEY", "\"$MAPS_API_KEY\"")
//            signingConfig = signingConfigs.getByName("release")
            isDebuggable = true
        }
        release {
            android.buildFeatures.buildConfig = true
            // the keys should be in local.properties
            // to be local and avoid API Keys check-in into Version Control System.
            buildConfigField("String", "API_BASE_URL", "\"https://api.openweathermap.org/data/2.5/\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"78e0c4ae0e0a6e63c7a1b82f34c7f8f2\"")
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("androidx.preference:preference-ktx:1.2.1")

    // UI
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Retrofit components (for API Calls)
    implementation("com.squareup.retrofit2:retrofit:2.6.4")
    implementation("com.squareup.retrofit2:converter-gson:2.6.4")

    // Google Services
    implementation("com.google.android.gms:play-services-location:21.0.1") // Location Service
    implementation("com.google.android.libraries.places:places:3.2.0") // Places Service
    implementation("com.google.android.gms:play-services-maps:18.1.0") // Map Services

    // Room components
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    val activityVersion = "1.7.2"
    implementation("androidx.activity:activity-ktx:$activityVersion")

    // Lifecycle components
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


//    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.10.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}