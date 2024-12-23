import com.android.build.gradle.LibraryExtension
import dev.angryl1on.vetclinic.convention.configureKotlinAndroid
import dev.angryl1on.vetclinic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
            }

            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("androidx-test-ext-junit-ktx").get())
                add("androidTestImplementation", libs.findLibrary("androidx-test-ext-junit-ktx").get())
                add("implementation", libs.findLibrary("jakewharton-timber").get())
            }
        }
    }
}
