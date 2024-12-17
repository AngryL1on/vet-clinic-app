plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.room)
}

android {
    namespace = "dev.angryl1on.vetclinic.database"

}

dependencies {
    /**
     * Module dependencies
     */
    implementation(project(":core:domain"))
}
