plugins {
    id(BuildPlugins.Android.library)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.kapt)
    id(BuildPlugins.daggerHilt)
}

android {
    namespace = "com.hardiksachan.auth_presentation"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.composeCompiler
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(Libs.core))
    implementation(project(Libs.coreUi))

    with(Libs.Auth) {
        implementation(project(domain))
        implementation(project(application))
    }

    with(Dependencies.AndroidX) {
        implementation(core)
        implementation(activity)
        implementation(lifecycle)
        implementation(navigationCompose)
        implementation(hilt)
    }

    with(Dependencies.AndroidX.Compose) {
        implementation(ui)
        implementation(uiToolingPreview)
        implementation(material3)

        androidTestImplementation(uiTest)

        debugImplementation(uiTooling)
        debugImplementation(uiTestManifest)
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