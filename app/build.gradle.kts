plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("org.jetbrains.kotlin.plugin.parcelize")
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
}

android {
    namespace = "com.thedach.kinovod"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.thedach.kinovod"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Jetpack Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.7")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")

    // RxJava Gson
    implementation("com.squareup.retrofit2:adapter-rxjava2:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // Picasso
    implementation("com.squareup.picasso:picasso:2.71828")


    implementation(libs.androidx.fragment)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}