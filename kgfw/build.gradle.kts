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

// Trigger if you want to try wayland
val generateWaylandProtocols = tasks.register<Exec>("generateWaylandProtocols") {
    dependsOn(unpackTask)

    val outputDir = libDestination.dir("rgfw").asFile
    val markerFile = libDestination.file("wayland-protocols.completed").asFile

    doFirst {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
    }

    workingDir(outputDir)

    // Generate all required Wayland protocol headers and compile them
    commandLine("sh", "-c", """
        wayland-scanner client-header /usr/share/wayland-protocols/stable/xdg-shell/xdg-shell.xml xdg-shell.h &&
        wayland-scanner private-code /usr/share/wayland-protocols/stable/xdg-shell/xdg-shell.xml xdg-shell.c &&
        wayland-scanner client-header /usr/share/wayland-protocols/unstable/xdg-decoration/xdg-decoration-unstable-v1.xml xdg-decoration-unstable-v1.h &&
        wayland-scanner private-code /usr/share/wayland-protocols/unstable/xdg-decoration/xdg-decoration-unstable-v1.xml xdg-decoration-unstable-v1.c &&
        gcc -c -fPIC xdg-shell.c -o xdg-shell.o $(pkg-config --cflags wayland-client) &&
        gcc -c -fPIC xdg-decoration-unstable-v1.c -o xdg-decoration-unstable-v1.o $(pkg-config --cflags wayland-client) &&
        ar rcs libwayland-protocols.a xdg-shell.o xdg-decoration-unstable-v1.o
    """.trimIndent())

    doLast {
        markerFile.writeText("generated ${Date()}")
    }

    outputs.file(markerFile)
    outputs.upToDateWhen { markerFile.exists() }
}

tasks.matching { it.name.startsWith("cinteropRgfw") }.configureEach {
    dependsOn(generateWaylandProtocols)
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
