plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "dev.angryl1on.vetclinic.domain"

}

dependencies {

    /**
     * Module dependencies
     */
    implementation(project(":core:model"))

    /**
     * Coroutines dependencies
     */
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.kotlinx.serialization.json)
}
