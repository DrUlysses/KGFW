@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    linuxX64 {
        binaries {
            executable {
                entryPoint = "main"
                linkerOpts("-L/usr/lib", "-L/usr/lib/x86_64-linux-gnu", "-lc")
                freeCompilerArgs += listOf("-linker-option", "--allow-shlib-undefined")
            }
        }
    }

    dependencies {
        implementation(project(":kgfw"))
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
        languageSettings.optIn("kotlinx.cinterop.UnsafeNumber")
        languageSettings.optIn("kotlin.time.ExperimentalTime")
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }
}
