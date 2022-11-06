plugins {
    id(BuildPlugins.Android.library)
    id(BuildPlugins.Kotlin.android)
}

android {
    namespace = "com.hardiksachan.core_ui"
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(project(Libs.core))
    with(Libs.Auth) {
        implementation(project(domain))
        implementation(project(application))
    }

    with(Dependencies.AndroidX) {
        implementation(core)
        implementation(activity)
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

    with(Dependencies.Arrow) {
        implementation(platform(bom))
        implementation(core)
    }
}