plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.org.gradle.android.cache.fix) apply false
    alias(libs.plugins.com.google.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false
}
