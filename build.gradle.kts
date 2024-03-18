

buildscript {
    dependencies {
       
        classpath ("com.google.gms:google-services:4.4.1")

    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false

    id("com.google.dagger.hilt.android") version "2.46.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
}