pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        // Define the version here
        id("com.android.application") version "8.4.2" apply false
        id("org.jetbrains.kotlin.android") version "2.1.0" apply false // <-- Corrected with parentheses
        id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
        // Add this line to define the Compose plugin version
        id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
        // Add other plugins here with their versions and apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Flight Search"
include(":app")
