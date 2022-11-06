object Dependencies {
    const val googleServices = "com.google.gms:google-services:${Version.googleServices}"

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Version.androidCore}"
        const val activity = "androidx.activity:activity-ktx:${Version.activity}"
        const val activityCompose = "androidx.activity:activity-compose:${Version.activityCompose}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
        const val navigationCompose = "androidx.navigation:navigation-compose:${Version.navigation}"

        object Compose {
            const val ui = "androidx.compose.ui:ui:${Version.compose}"
            const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Version.compose}"
            const val material3 = "androidx.compose.material3:material3:${Version.composeMaterial}"

            // android Test
            const val uiTest = "androidx.compose.ui:ui-test-junit4:${Version.compose}"

            // debug
            const val uiTooling = "androidx.compose.ui:ui-tooling:${Version.compose}"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Version.compose}"
        }

        object Test {
            const val jUnitExtension = "androidx.test.ext:junit:1.1.3"
            const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        }
    }

    const val kotlinXDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.datetime}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"

    object Arrow {
        const val bom = "io.arrow-kt:arrow-stack:${Version.arrow}"
        const val core = "io.arrow-kt:arrow-core"
        const val coroutines = "io.arrow-kt:arrow-fx-coroutines"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Version.firebase}"
        const val auth = "com.google.firebase:firebase-auth-ktx"
    }

    const val googleAuth = "com.google.android.gms:play-services-auth:${Version.googleAuth}"

    object Kotest {
        const val runner = "io.kotest:kotest-runner-junit5:${Version.kotest}"
        const val datatest = "io.kotest:kotest-framework-datatest:${Version.kotest}"
    }

    const val turbine = "app.cash.turbine:turbine:${Version.turbine}"

    object Strikt {
        const val core = "io.strikt:strikt-core:${Version.strikt}"
        const val arrow = "io.strikt:strikt-arrow:${Version.strikt}"
        const val filePeek = "com.christophsturm:filepeek:${Version.filepeek}"
    }

    object MockK {
        const val core = "io.mockk:mockk:${Version.mockK}"
        const val android = "io.mockk:mockk-android:${Version.mockK}"
        const val agent = "io.mockk:mockk-agent:${Version.mockK}"
    }
}