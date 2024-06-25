package dev.adriankuta.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

@Suppress("UnstableApiUsage")
internal fun configureAndroid(commonExtension: CommonExtension<*, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = 34

        defaultConfig {
            minSdk = 24
            //testInstrumentationRunner = "com.google.samples.modularization.testing.HiltTestRunner"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }



        buildFeatures.buildConfig = false
    }
}