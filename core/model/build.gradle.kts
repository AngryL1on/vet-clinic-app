plugins {
    alias(libs.plugins.angryl1on.vetclinic.android.library)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "dev.angryl1on.vetclinic.model"

}

dependencies{
    implementation(libs.kotlinx.serialization.json)
}
