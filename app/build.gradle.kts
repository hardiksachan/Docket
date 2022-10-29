plugins {
    id(BuildPlugins.Android.application)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.googleServices)
}

android {
    namespace = AndroidSDK.namespace
    compileSdk = AndroidSDK.compileSdk

    defaultConfig {
        applicationId = AndroidSDK.DefaultConfig.applicationId
        minSdk = AndroidSDK.DefaultConfig.minSdk
        targetSdk = AndroidSDK.DefaultConfig.targetSdk
        versionCode = AndroidSDK.DefaultConfig.versionCode
        versionName = AndroidSDK.DefaultConfig.versionName

        testInstrumentationRunner = AndroidSDK.DefaultConfig.instrumentationRunner

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(ProGuardFile.textFile), ProGuardFile.ruleFile
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()  // 1.8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.composeCompiler
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(Libs.Kotlin.core))

    with(Dependencies.AndroidX) {
        implementation(core)
        implementation(activityCompose)
        implementation(lifecycle)
    }

    with(Dependencies.AndroidX.Compose) {
        implementation(ui)
        implementation(uiToolingPreview)
        implementation(material3)

        androidTestImplementation(uiTest)

        debugImplementation(uiTooling)
        debugImplementation(uiTestManifest)
    }

    implementation(Dependencies.kotlinXDateTime)
    implementation(Dependencies.kotlinReflect)

    with(Dependencies.Arrow) {
        implementation(platform(bom))
        implementation(core)
        implementation(coroutines)
    }


    with(Dependencies.Firebase) {
        implementation(platform(bom))
        implementation(auth)
    }
    implementation(Dependencies.googleAuth)

    with(Dependencies.Kotest) {
        testImplementation(runner)
        testImplementation(datatest)
    }

    testImplementation(Dependencies.turbine)

    with(Dependencies.Strikt) {
        testImplementation(core)
        testImplementation(arrow)
        testImplementation(filePeek)
    }

    with(Dependencies.MockK) {
        testImplementation(core)
        testImplementation(android)
        testImplementation(agent)
    }

    // coroutines test
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:1.6.4")
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")


    // Misc
    with(Dependencies.AndroidX.Test) {
        androidTestImplementation(jUnitExtension)
        androidTestImplementation(espresso)
    }
}