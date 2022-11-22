plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.Kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(Dependencies.uuid)

    with(Dependencies.Arrow) {
        implementation(platform(bom))
        implementation(core)
    }
}