plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hardcoreamature.chefiq"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hardcoreamature.chefiq"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    packagingOptions {
        exclude("META-INF/INDEX.LIST")
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

    kotlinOptions {
        jvmTarget = "17" // Or whatever your target version is
        freeCompilerArgs += "-Xjvm-default=all"
    }

    kapt {
        javacOptions {
            option("-source", JavaVersion.VERSION_17.toString())
            option("-target", JavaVersion.VERSION_17.toString())
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-inappmessaging:20.4.0")

    // Google Play services
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("com.google.android.gms:play-services-vision-face-contour-internal:16.1.0")
    // Firebase ML Kit
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    implementation("com.google.firebase:firebase-ml-vision-face-model:20.0.2")
    implementation("com.google.firebase:firebase-ml-model-interpreter:22.0.4")

    // CameraX dependencies
    implementation("androidx.camera:camera-camera2:1.4.0-alpha02")
    implementation("androidx.camera:camera-lifecycle:1.4.0-alpha02")
    implementation("androidx.camera:camera-view:1.4.0-alpha02")

    // AndroidX Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-vision-face-contour-internal:16.1.0")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
