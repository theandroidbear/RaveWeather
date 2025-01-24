plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ravemaster.raveweather"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ravemaster.raveweather"
        minSdk = 26
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.retrofit)
    implementation(libs.gsonConverter)
    implementation(libs.okhttp3)
    implementation(libs.logging.interceptor)

    implementation(libs.glide)
    implementation(libs.shimmer.android)
    implementation(libs.splashscreen)
    implementation(libs.lottie)
    implementation(libs.swiperefresh)
    implementation(libs.multidex)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}