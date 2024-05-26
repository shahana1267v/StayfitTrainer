// Top-level build file where you can add configuration options common to all sub-projects/modules.


// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {

    }
    dependencies {
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}


plugins {
    id 'com.android.application' version '8.3.0' apply false
    id 'com.android.library' version '8.3.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.20' apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}