plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.angryl1on.vetclinic.android.koin)
}

android {
    namespace = "dev.angryl1on.vetclinic.testing"

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/LICENSE-notice.md",
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE.txt",
                "META-INF/LICENSE.txt"
            )
        }
    }
}

dependencies {
    /**
     * Common testing dependencies
     */
    api(libs.androidx.test.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.ext.junit.ktx)
    api(libs.kotlinx.coroutines.test)

    /**
     * Unit testing dependencies
     */
    api(libs.junit)
    api(libs.io.mockk)
    api(libs.io.mockk.agent)
    api(libs.io.insert.koin.test)
    api(libs.io.insert.koin.test.junit4)

    /**
     * Instrumented testing dependencies
     */
    api(libs.espresso.core)
    api(libs.androidx.test.ext.junit.ktx)
}
