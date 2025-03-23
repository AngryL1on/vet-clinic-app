plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.library.compose)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.auth"

}

dependencies {

    /**
     * Core dependencies
     */
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)

    /**
     *  Module dependencies
     */
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
