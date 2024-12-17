plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library.flavored)
    alias(libs.plugins.angryl1on.vetclinic.android.ktor)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.network"

    productFlavors {
        getByName("dev") {
            dimension = "environment"
            buildConfigField(
                "String",
                "WEB_PORTAL_HOST",
                // TODO: Change URL
                "\"dev.localhost.ru\""
            )
        }

        getByName("preprod") {
            dimension = "environment"
            buildConfigField(
                "String",
                "WEB_PORTAL_HOST",
                // TODO: Change URL
                "\"preprod.localhost.ru\""
            )
        }

        getByName("prod") {
            dimension = "environment"
            buildConfigField(
                "String",
                "WEB_PORTAL_HOST",
                // TODO: Change URL
                "\"localhost.ru\""
            )
        }
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
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    testImplementation(project(":core:testing"))
}
