import dev.angryl1on.vetclinic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findLibrary("io-insert-koin-core").get())
                add("implementation", libs.findLibrary("io-insert-koin-androidx").get())
                add("implementation", libs.findLibrary("io-insert-koin-androidx-compose").get())
            }
        }
    }
}
