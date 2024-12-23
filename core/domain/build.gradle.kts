plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
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
}
