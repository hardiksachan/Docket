pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Docket"
include(":app")
include(":core")
include(":auth:auth-domain")
include(":auth:auth-application")
include(":auth:auth-infrastructure")
include(":auth:auth-framework")
include(":auth:auth-presentation")
