plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.common"

}

dependencies {
    /**
     * Module dependencies
     */
    implementation(project(":core:domain"))

    /**
     * Core dependencies
     */
    implementation(libs.androidx.core.ktx)
}
