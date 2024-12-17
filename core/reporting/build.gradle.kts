plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
}

android {
    namespace = "dev.angryl1on.vetclinic.reporting"

}

dependencies {
    /**
     * Module dependencies
     */
    implementation(project(":core:domain"))
}
