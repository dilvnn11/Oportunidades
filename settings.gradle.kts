pluginManagement {
    repositories {
        google()
        maven("https://cache-redirector.jetbrains.com/maven-central")
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://cache-redirector.jetbrains.com/maven-central")
        mavenCentral()
    }
}
rootProject.name = "Oportunidades"
include(":app")
