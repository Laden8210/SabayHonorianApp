plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.sabayhonorianapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sabayhonorianapp"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.location)
    implementation(libs.core.ktx)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.shuhart:stepview:1.5.1")
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.13")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:16.0.0")

    implementation ("com.mapbox.maps:android:10.16.0")

    implementation ("com.mapbox.mapboxsdk:mapbox-sdk-turf:5.0.0")
    implementation ("com.mapbox.navigation:android:2.15.2")
    implementation ("com.mapbox.navigation:android:2.19.0")
    implementation ("com.mapbox.search:mapbox-search-android-ui:1.0.0-rc.6")

}