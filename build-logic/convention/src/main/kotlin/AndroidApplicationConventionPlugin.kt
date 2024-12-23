import com.android.build.api.dsl.ApplicationExtension
import dev.angryl1on.vetclinic.convention.configureKotlinAndroid
import dev.angryl1on.vetclinic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.gradle.android.cache-fix")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
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
