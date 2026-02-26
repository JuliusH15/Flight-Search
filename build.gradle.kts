// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    // Use the alias for the application plugin. The version is managed in settings.gradle.kts
    alias(libs.plugins.android.application) apply false

    // Use the alias for the Kotlin Android plugin. The version is managed in settings.gradle.kts
    alias(libs.plugins.kotlin.android) apply false

    // Use the alias for the Kotlin Compose plugin. The version is managed in settings.gradle.kts
    alias(libs.plugins.kotlin.compose) apply false

    // Use the alias for the KSP plugin. The version is managed in settings.gradle.kts
    alias(libs.plugins.ksp) apply false
}