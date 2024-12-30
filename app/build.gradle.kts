plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.easydine"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.easydine"
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

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("androidx.room:room-runtime:2.6.0")
//    kapt ("androidx.room:room-compiler:$room_version")
//    ksp("libs.androidx.room.compiler")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    testImplementation ("androidx.room:room-testing:2.6.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.11.0")
    // Moshi for JSON parsing
    implementation ("com.squareup.moshi:moshi:1.15.1")
    implementation (libs.moshi.kotlin)

    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    // OkHttp for logging interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt ("com.github.bumptech.glide:compiler:4.15.1")

    // Testing Libraries
//    testImplementation ("junit:junit:4.13.2")
//    testImplementation ("org.mockito:mockito-core:4.11.0")
//    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
//    testImplementation ("androidx.arch.core:core-testing:2.1.0")
//    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
}
