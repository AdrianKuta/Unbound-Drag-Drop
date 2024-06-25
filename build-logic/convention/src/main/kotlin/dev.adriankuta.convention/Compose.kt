package dev.adriankuta.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("UnstableApiUsage")
internal fun Project.configureCompose(commonExtension: BaseExtension) {
    commonExtension.apply {
        buildFeatures.apply {
            compose = true
        }

        val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("androidxComposeCompiler").get().toString()
        }

        dependencies {
            add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
            add("debugImplementation", platform(libs.findLibrary("androidx.compose.bom").get()))
            add("testImplementation", platform(libs.findLibrary("androidx.compose.bom").get()))
            add(
                "androidTestImplementation",
                platform(libs.findLibrary("androidx.compose.bom").get())
            )

//            add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test.junit4").get())
//            add("androidTestImplementation", project(":core:testing"))
        }
    }
}