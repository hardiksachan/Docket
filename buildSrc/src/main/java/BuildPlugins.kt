object BuildPlugins {
    object Android {
        const val application = "com.android.application"
        const val library = "com.android.library"
    }

    object Kotlin {
        const val jvm = "org.jetbrains.kotlin.jvm"
        const val android = "org.jetbrains.kotlin.android"
    }

    const val googleServices = "com.google.gms.google-services"
    const val javaLibrary = "java-library"
}

object Libs {
    const val core = ":core"

    object Auth {
        const val domain = ":auth:auth-domain"
        const val application = ":auth:auth-application"
        const val infrastructure = ":auth:auth-infrastructure"
    }
}