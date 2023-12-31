

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.gallery"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.gallery"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://hihi\"")
            buildConfigField(("String"), "API_KEY", "\"123\"" )
        }
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
    configurations.all {
        resolutionStrategy.force ("com.google.code.findbugs:jsr305:3.0.2")
    }


//    -----------------------------------------------------------------
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }

    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/DEPENDENCIES.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
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
    implementation("androidx.recyclerview:recyclerview:1.1.0")

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

    // google drive api services
    implementation("com.google.api-client:google-api-client:2.0.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Add the dependency for the Realtime Database library
    implementation("com.google.firebase:firebase-database")

    // reactive for using async task
    implementation("io.reactivex.rxjava2:rxjava:2.1.9")

    // R2xAndroidNetworking for Rest API calling and fetching data from remote
    implementation("com.amitshekhar.android:rx2-android-networking:1.0.1")

    // facebook apis
    implementation("com.facebook.android:facebook-login:15.2.0")
    implementation("com.facebook.android:facebook-messenger:15.2.0")
    implementation("com.facebook.android:facebook-share:15.2.0")

    // google play services
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Librabry for QR code generation
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.2")
    implementation ("androidx.multidex:multidex:2.0.1")

    // Add Gson
    implementation ("com.google.code.gson:gson:2.10.1")

    // PhotoView
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")

    // Video player exo player
    implementation ("androidx.media3:media3-exoplayer:1.2.0")
    implementation ("androidx.media3:media3-exoplayer-dash:1.2.0")
    implementation ("androidx.media3:media3-ui:1.2.0")

    // biometric for fingerprint
    implementation("androidx.biometric:biometric:1.1.0")

    implementation("com.google.android.material:material:1.5.0")

    // Add the dependency to recognize text from image
    implementation("com.google.firebase:firebase-ml-vision:24.0.3")

    // dependency for image metadata
    implementation("com.drewnoakes:metadata-extractor:2.19.0")

    implementation("com.google.firebase:firebase-storage:latest")

    // Jcodec
    implementation ("org.jcodec:jcodec:0.2.5")
    implementation ("org.jcodec:jcodec-android:0.2.5")





}