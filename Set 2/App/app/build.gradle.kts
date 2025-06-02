plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.weathertrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weathertrack"
        minSdk = 21
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Room for database
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    // WorkManager for background jobs
    implementation ("androidx.work:work-runtime:2.10.1")

    // Lifecycle for ViewModel and LiveData
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // MPAndroidChart for temperature graph
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


}