plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.ktor)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.network"

    defaultConfig {
        buildConfigField("String", "WEB_HOST", "\"192.168.3.68\"")
    }

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    /**
     * Module dependencies
     */
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    testImplementation(project(":core:testing"))
}
