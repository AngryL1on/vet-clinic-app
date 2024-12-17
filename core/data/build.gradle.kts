plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.data"

}

dependencies {
    /**
     * Module dependencies
     */
    implementation(project(":core:domain"))
    implementation(project(":core:model"))

    implementation(libs.androidx.datastore.preferences)
}
