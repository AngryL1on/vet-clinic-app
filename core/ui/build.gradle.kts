plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library.compose)
}

android {
    namespace = "dev.angryl1on.vetclinic.ui"

}

dependencies {

    /**
     * Core dependencies
     */
    implementation(libs.androidx.core.ktx)

    /**
     * Navigation dependencies
     */
    implementation(libs.androidx.navigation.compose)

    /**
     *  Module dependencies
     */
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}
