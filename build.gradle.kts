// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false

}

buildscript {
    repositories {
        maven("https://www.jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
    }
}