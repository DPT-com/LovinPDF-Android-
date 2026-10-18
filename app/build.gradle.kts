plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lovinpdf.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lovinpdf.app"
        minSdk = 23
        targetSdk = 35
        versionCode = 3
        versionName = "1.2"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-ads:23.6.0")
}
