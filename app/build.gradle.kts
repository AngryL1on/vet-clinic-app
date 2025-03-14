plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.application)
    alias(libs.plugins.angryl1on.vetclinic.android.application.compose)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "dev.angryl1on.vetclinic"
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            versionNameSuffix = "-dev"
        }

        create("preprod") {
            dimension = "environment"
            versionNameSuffix = "-preprod"
        }

        create("prod") {
            dimension = "environment"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    /**
     * Core dependencies
     */
    implementation(libs.androidx.core.ktx)

    /**
     *  Module dependencies
     */
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:reporting"))
    implementation(project(":core:ui"))
    implementation(project(":feature:ui-main"))
    implementation(project(":feature:ui-appointment"))
    implementation(project(":feature:ui-history"))
    implementation(project(":feature:ui-profile"))

    /**
     * Navigation dependencies
     */
    implementation(libs.androidx.navigation.compose)
}
