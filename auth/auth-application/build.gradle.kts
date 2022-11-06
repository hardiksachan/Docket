plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.Kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    with(Libs) {
        implementation(project(core))
        implementation(project(Libs.Auth.domain))
    }

    implementation(Dependencies.coroutines)

    with(Dependencies.Arrow) {
        implementation(platform(bom))
        implementation(core)
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
        testImplementation(agent)
    }
}