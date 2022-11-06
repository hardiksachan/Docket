plugins {
    id(BuildPlugins.Android.library)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.kapt)
    id(BuildPlugins.daggerHilt)
}

android {
    namespace = "com.hardiksachan.auth_framework"
    compileSdk = AndroidSDK.compileSdk

    defaultConfig {
        minSdk = AndroidSDK.DefaultConfig.minSdk
        targetSdk = AndroidSDK.DefaultConfig.targetSdk

        testInstrumentationRunner = AndroidSDK.DefaultConfig.instrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.composeCompiler
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    with(Libs) {
        implementation(project(core))
        implementation(project(Libs.Auth.domain))
    }

    with(Dependencies.AndroidX) {
        implementation(core)
        implementation(activityCompose)
        implementation(Dependencies.AndroidX.Compose.runtime)
    }

    with(Dependencies.Dagger) {
        implementation(hiltAndroid)
        kapt(hiltAndroidCompiler)
    }

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
}