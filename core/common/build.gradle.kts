plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.common"

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    /**
     * Module dependencies
     */
    implementation(project(":core:domain"))
    testImplementation(project(":core:testing"))

    /**
     * Core dependencies
     */
    implementation(libs.androidx.core.ktx)
}
