plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.gallery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gallery"
        minSdk = 24
        targetSdk = 33
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

//    -----------------------------------------------------------------
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }


}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    ---------------------------------------------------------
    implementation("android.arch.lifecycle:extensions:1.1.1")

    // recyclerView
    implementation("com.androidx.support:recyclerview-v7:28.0.0")

    // room database
    implementation("androidx.room:room-runtime:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.8.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.8.0")

    // image management lib, has multimedia , including gif files
    implementation("com.facebook.fresco:fresco:3.1.3")

    // for Rest API calling and fetching data from remote
    implementation("com.squareup.retrofit2:retrofit:2.4.0")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
}