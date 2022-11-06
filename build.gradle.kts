buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.googleServices)
        classpath(Dependencies.hiltAndroidGradlePlugin)
    }
}

plugins {
    id(BuildPlugins.Android.application) version Version.gradle apply false
    id(BuildPlugins.Android.library) version Version.gradle apply false
    id(BuildPlugins.Kotlin.android) version Version.kotlin apply false
    id(BuildPlugins.Kotlin.jvm) version Version.kotlin apply false
}