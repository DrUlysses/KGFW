package examples

import kgfw.image.ImageTexture
import kgfw.image.readImageRGBA
import kotlinx.cinterop.*
import platform.opengl32.*
import platform.posix.F_OK
import platform.posix.access
import rgfw.*

var texture: ImageTexture? = null

/**
 * Minimal example demonstrating how to use kgfw.ImageTexture.
 *
 * This example generates a simple checkerboard RGBA ByteArray.
 * Uploads it as an OpenGL texture via ImageTexture, and draws it at the mouse position.
 *
 * Notes:
 * - If you want to load a PNG/JPG instead, decode it to a tightly packed RGBA ByteArray of size
 *   (width * height * 4) first (e.g., via stb_image or another decoder), then pass it to ImageTexture.
 */
actual fun texture(
    windowPointer: CPointer<RGFW_window>,
    windowWidth: Int,
    windowHeight: Int
): (windowPointer: CPointer<RGFW_window>) -> Unit {
    // Prepare the texture
    if (texture == null) {
        // Try to load the Kodee PNG from known relative paths
        val kodeePath = findKodeePngPath()
        var decoded = kodeePath?.let { readImageRGBA(it) }

        if (decoded != null) {
            println("Loaded image: ${decoded.width}x${decoded.height}, size: ${decoded.data.size} bytes")
            
            // Check against MAX_TEXTURE_SIZE (let's assume 1024 as a safe default if we can't query it here)
            // Actually, we should probably do this inside ImageTexture or handle it here.
            // Since we know 1024 is the limit on this machine, let's target that.
            val limit = 1024
            if (decoded.width > limit || decoded.height > limit) {
                println("Image too large for some GPUs, downsampling...")
                decoded = downsampleRGBA(decoded, limit)
                println("Downsampled to: ${decoded.width}x${decoded.height}")
            }
        }

        texture = if (decoded != null) {
            ImageTexture(
                imageData = decoded.data,
                width = decoded.width,
                height = decoded.height
            )
        } else {
            // Fallback: generated checkerboard (POT for compatibility)
            val potSize = 256
            ImageTexture(
                imageData = generateCheckerboardRgba(
                    width = potSize,
                    height = potSize,
                    cellSize = 16
                ),
                width = potSize,
                height = potSize
            )
        }
    }

    texture?.let { texture ->
        // Clear screen
        glClearColor(0.1f, 0.1f, 0.12f, 1f)
        glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())

        // Draw the texture at the current mouse position (top-left anchored)
        val (w, h) = memScoped {
            val wVar = alloc<IntVar>()
            val hVar = alloc<IntVar>()
            RGFW_window_getSize(windowPointer, wVar.ptr, hVar.ptr)
            Pair(wVar.value, hVar.value)
        }
        texture.drawScaled(windowPointer, 0, 0, h, w)
    }

    return {
        println("Disposing texture example")
        texture?.dispose()
        texture = null
    }
}

private fun generateCheckerboardRgba(
    width: Int,
    height: Int,
    cellSize: Int
): ByteArray {
    println("Generating fallback checkerboard ${width}x${height}")
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
    val paths = listOf(
        "example/src/nativeMain/resources/Kodee_Assets_Digital_Kodee-greeting.png",
        "src/nativeMain/resources/Kodee_Assets_Digital_Kodee-greeting.png"
    )
    for (path in paths) {
        if (access(path, F_OK) == 0) {
            println("Found Kodee PNG at: $path")
            return path
        }
    }
    println("Kodee PNG not found, using fallback")
    return null
}

private fun downsampleRGBA(source: kgfw.image.DecodedImage, limit: Int): kgfw.image.DecodedImage {
    var targetWidth = source.width
    var targetHeight = source.height
    
    // Scale down to limit
    if (targetWidth > limit || targetHeight > limit) {
        val ratio = limit.toDouble() / maxOf(targetWidth, targetHeight)
        targetWidth = (targetWidth * ratio).toInt()
        targetHeight = (targetHeight * ratio).toInt()
    }
    
    // Further scale down to nearest power of two for maximum compatibility with legacy OpenGL
    targetWidth = floorToPOT(targetWidth)
    targetHeight = floorToPOT(targetHeight)
    
    if (targetWidth <= 0) targetWidth = 1
    if (targetHeight <= 0) targetHeight = 1
    
    val newData = ByteArray(targetWidth * targetHeight * 4)
    for (y in 0 until targetHeight) {
        for (x in 0 until targetWidth) {
            val srcX = (x.toDouble() / targetWidth * source.width).toInt().coerceIn(0, source.width - 1)
            val srcY = (y.toDouble() / targetHeight * source.height).toInt().coerceIn(0, source.height - 1)
            
            val srcIdx = (srcY * source.width + srcX) * 4
            val dstIdx = (y * targetWidth + x) * 4
            
            newData[dstIdx] = source.data[srcIdx]
            newData[dstIdx + 1] = source.data[srcIdx + 1]
            newData[dstIdx + 2] = source.data[srcIdx + 2]
            newData[dstIdx + 3] = source.data[srcIdx + 3]
        }
    }
    
    return kgfw.image.DecodedImage(newData, targetWidth, targetHeight)
}

private fun floorToPOT(value: Int): Int {
    if (value <= 0) return 0
    var pot = 1
    while (pot * 2 <= value) {
        pot *= 2
    }
    return pot
}
