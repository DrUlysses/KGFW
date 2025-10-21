import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.register
import java.util.Date

val artifact = "kgfw"
group = "dr.ulysses"
version = "1.0.0"

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.undercouch.download)
    `maven-publish`
}

kotlin {
    applyDefaultHierarchyTemplate()

    listOf(
        linuxX64(),
        mingwX64(),
        macosX64()
    ).forEach {
        it.compilations.getByName("main").cinterops {
            val rgfw by creating {
                includeDirs.allHeaders(layout.buildDirectory.dir("interopLib").get().dir("rgfw"))
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
        languageSettings.optIn("kotlinx.cinterop.UnsafeNumber")
        languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }
}

val libDestination = layout.buildDirectory.dir("interopLib").get().apply { asFile.mkdirs() }

val downloadRGFW = tasks.register<Download>("downloadRGFW") {
    src("https://github.com/ColleagueRiley/RGFW/archive/refs/tags/${libs.versions.rgfw.get()}.zip")
    dest(libDestination.file("rgfw.zip").asFile)
    overwrite(true)
    // Always ensure the zip is present when needed
    outputs.file(libDestination.file("rgfw.zip"))
    outputs.upToDateWhen { false }
}

val unpackTask = tasks.register<Copy>("unpackSdk") {
    val zipFileInput = libDestination.file("rgfw.zip")
    val markerFile = libDestination.file("$name.completed")
    val outputDir = libDestination.dir("rgfw")

    dependsOn(downloadRGFW)

    doFirst {
        if (outputDir.asFile.exists()) {
            outputDir.asFile.deleteRecursively()
        }
    }

    from(zipTree(zipFileInput.asFile)) {
        eachFile {
            path = path.split("/", limit = 2)[1]
        }
        includeEmptyDirs = false
    }
    into(outputDir)

    doLast {
        if (zipFileInput.asFile.exists()) {
            zipFileInput.asFile.delete()
        }
        markerFile.asFile.writeText("generated ${Date()}")
    }

    inputs.file(zipFileInput)
    outputs.dir(outputDir)
    outputs.file(markerFile)
}

tasks.matching { it.name.startsWith("cinteropRgfw") }.configureEach {
    dependsOn(unpackTask)
}

publishing {
    publications.withType<MavenPublication> {
        groupId = project.group.toString()
        version = project.version.toString()
        artifactId = when (name) {
            "kotlinMultiplatform" -> artifact
            else -> "$artifact-$name"
        }

        pom {
            name = artifact
            description = "Kotlin/Native bindings for GLFW"
            url = "https://github.com/DrUlysses/kgfw"
        }

        repositories {
            mavenLocal()
        }
    }
}
