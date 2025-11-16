package examples

import kgfw.image.ImageTexture
import kgfw.image.readImageRGBA
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.pointed
import platform.posix.F_OK
import platform.posix.access
import rgfw.*

var texture: ImageTexture? = null

/**
 * Minimal example demonstrating how to use kgfw.ImageTexture.
 *
 * This example generates a simple checkerboard RGBA ByteArray (so we don't need an image decoder),
 * uploads it as an OpenGL texture via ImageTexture, and draws it at the mouse position.
 *
 * Notes:
 * - If you want to load a PNG/JPG instead, decode it to a tightly packed RGBA ByteArray of size
 *   (width * height * 4) first (e.g., via stb_image or another decoder), then pass it to ImageTexture.
 */
fun texture(
    windowPointer: CPointer<RGFW_window>
): (windowPointer: CPointer<RGFW_window>) -> Unit {
    // Prepare the texture
    if (texture == null) {
        // Try to load the Kodee PNG from known relative paths
        val kodeePath = findKodeePngPath()
        val decoded = kodeePath?.let { readImageRGBA(it) }
        texture = if (decoded != null) {
            ImageTexture(
                imageData = decoded.data,
                width = decoded.width,
                height = decoded.height
            )
        } else {
            // Fallback: generated checkerboard
            val width = windowPointer.pointed.r.w
            val height = windowPointer.pointed.r.h
            ImageTexture(
                imageData = generateCheckerboardRgba(
                    width = width,
                    height = height,
                    cellSize = 16
                ),
                width = width,
                height = height
            )
        }
    }

    texture?.let { texture ->
        // Clear screen
        glClearColor(0.1f, 0.1f, 0.12f, 1f)
        glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())

        // Draw the texture at the current mouse position (top-left anchored)
        texture.drawScaled(windowPointer, 0, 0, windowPointer.pointed.r.h, windowPointer.pointed.r.w)
    }

    return {
        texture?.dispose()
        texture = null
    }
}

private fun generateCheckerboardRgba(
    width: Int,
    height: Int,
    cellSize: Int
): ByteArray {
    val data = ByteArray(width * height * 4)
    var i = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val toggle = ((x / cellSize + y / cellSize) % 2) == 0
            val r: Int
            val g: Int
            val b: Int
            if (toggle) {
                r = 255; g = 255; b = 255 // white
            } else {
                r = 180; g = 50; b = 200 // magenta-ish
            }
            data[i++] = r.toByte()
            data[i++] = g.toByte()
            data[i++] = b.toByte()
            data[i++] = 255.toByte()   // alpha
        }
    }
    return data
}

private fun findKodeePngPath(): String? {
    val path = "src/nativeMain/resources/Kodee_Assets_Digital_Kodee-greeting.png"
    return if (access(path, F_OK) == 0) {
        path
    } else {
        null
    }
}

