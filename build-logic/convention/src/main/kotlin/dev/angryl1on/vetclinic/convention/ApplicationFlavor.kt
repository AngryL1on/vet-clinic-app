package dev.angryl1on.vetclinic.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

@Suppress("EnumEntryName")
enum class FlavorDimension {
    environment
}

@Suppress("EnumEntryName")
enum class ApplicationFlavor(val dimension: FlavorDimension) {
    dev(FlavorDimension.environment),
    preprod(FlavorDimension.environment),
    prod(FlavorDimension.environment)
}

fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: ApplicationFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.environment.name
        productFlavors {
            ApplicationFlavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)
                }
            }
        }
    }
}
