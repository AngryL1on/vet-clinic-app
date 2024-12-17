import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "dev.angryl1on.vetclinic.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.gradle.plugin.android)
    compileOnly(libs.gradle.plugin.kotlin)
    compileOnly(libs.gradle.plugin.ksp)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "angryl1on.vetclinic.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "angryl1on.vetclinic.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "angryl1on.vetclinic.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidFlavoredLibrary") {
            id = "angryl1on.vetclinic.android.library.flavored"
            implementationClass = "AndroidFlavoredLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "angryl1on.vetclinic.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidKtor") {
            id = "angryl1on.vetclinic.android.ktor"
            implementationClass = "AndroidKtorConventionPlugin"
        }

        register("androidKoin") {
            id = "angryl1on.vetclinic.android.koin"
            implementationClass = "AndroidKoinConventionPlugin"
        }

        register("androidRoom") {
            id = "angryl1on.vetclinic.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}
