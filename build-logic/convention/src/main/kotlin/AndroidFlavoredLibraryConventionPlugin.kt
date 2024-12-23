import com.android.build.gradle.LibraryExtension
import dev.angryl1on.vetclinic.convention.configureFlavors
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidFlavoredLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("angryl1on.vetclinic.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureFlavors(this)
            }
        }
    }
}
