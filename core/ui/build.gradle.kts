plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library.compose)
}

android {
    namespace = "ru.angryl1on.vetclinic.ui"

}

dependencies {
    /**
     * Core dependencies
     */
    implementation(libs.androidx.core.ktx)

    /**
     *  Module dependencies
     */
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}
