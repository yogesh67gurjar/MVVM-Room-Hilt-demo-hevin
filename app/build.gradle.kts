plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.yogeshandroid.practice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yogeshandroid.practice"
        minSdk = 25
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // dagger hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.gson.converter)

    // picasso for image processing
    implementation(libs.picasso)

    // viewmodel scope
    implementation(libs.viewModel.scope)

    // sdp ssp
    implementation (libs.sdp.android)
    implementation (libs.ssp.android)

    // circle imageview
    implementation (libs.circleimageview)


}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}