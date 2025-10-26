import de.undercouch.gradle.tasks.download.Download
import org.gradle.kotlin.dsl.register
import java.util.Date

val artifact = "kgfw"
group = "io.github.drulysses"
version = "1.0.0"

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.undercouch.download)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
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
    outputs.file(libDestination.file("rgfw.zip"))
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

// Patch RGFW.h to disable DPI scaling for MinGW compatibility
val patchRGFWTask = tasks.register("patchRGFW") {
    dependsOn(unpackTask)

    val rgfwHeader = libDestination.dir("rgfw").asFile.resolve("RGFW.h")
    val markerFile = libDestination.file("patch.completed")

    inputs.file(rgfwHeader)
    outputs.file(markerFile)

    doLast {
        if (rgfwHeader.exists()) {
            var content = rgfwHeader.readText()

            // Add RGFW_NO_DPI define at the top if not already present
            if (!content.contains("#define RGFW_NO_DPI")) {
                // Insert after the first #ifndef RGFW_H or at the beginning
                val insertPoint = content.indexOf("#ifndef RGFW_H")
                if (insertPoint != -1) {
                    val lineEnd = content.indexOf('\n', insertPoint) + 1
                    content = content.take(lineEnd) +
                            "#ifndef RGFW_NO_DPI\n#define RGFW_NO_DPI\n#endif\n\n" +
                            content.substring(lineEnd)
                } else {
                    content = "#ifndef RGFW_NO_DPI\n#define RGFW_NO_DPI\n#endif\n\n$content"
                }

                rgfwHeader.writeText(content)
                println("Patched RGFW.h to define RGFW_NO_DPI")
            }

            markerFile.asFile.writeText("patched ${Date()}")
        }
    }
}

// Trigger if you want to try wayland
val generateWaylandProtocols = tasks.register<Exec>("generateWaylandProtocols") {
    dependsOn(patchRGFWTask)

    // Write Wayland artifacts into a dedicated subdirectory to avoid overlapping with inputs of other cinterop tasks
    val waylandDir = libDestination.dir("rgfw").dir("wayland").asFile
    val libFile = waylandDir.resolve("libwayland-protocols.a")

    doFirst {
        if (!waylandDir.exists()) {
            waylandDir.mkdirs()
        }
    }

    workingDir(waylandDir)

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

    outputs.file(libFile)
}

tasks.matching { it.name.startsWith("cinteropRgfw") }.configureEach {
    dependsOn(patchRGFWTask)
}

// Wayland protocol generation is Linux-only
tasks.matching { it.name.startsWith("cinteropRgfw") && it.name.contains("LinuxX64") }.configureEach {
    dependsOn(generateWaylandProtocols)
}

// Ensure task ordering to avoid implicit dependency validation errors: even non-Linux cinterops must run after Wayland generation when both are in the graph
tasks.matching { it.name == "cinteropRgfwMingwX64" }.configureEach {
    mustRunAfter(generateWaylandProtocols)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = group.toString(),
        artifactId = artifact,
        version = version.toString()
    )

    pom {
        name = artifact
        description = "Kotlin/Native bindings for GLFW"
        url = "https://github.com/DrUlysses/KGFW"
        inceptionYear = "2025"

        developers {
            developer {
                id = "DrUlysses"
                url = "https://github.com/DrUlysses"
            }
        }

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        scm {
            url = "https://github.com/DrUlysses/KGFW"
            connection = "scm:git:git://github.com/DrUlysses/KGFW.git"
            developerConnection = "scm:git:ssh://git@github.com:DrUlysses/KGFW.git"
        }
    }
}
