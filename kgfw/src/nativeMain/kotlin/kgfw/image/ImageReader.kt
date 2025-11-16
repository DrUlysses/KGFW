package kgfw.image

import kotlinx.cinterop.*
import platform.posix.memcpy
import stb_image.stbi_image_free
import stb_image.stbi_load

data class DecodedImage(
    val data: ByteArray,
    val width: Int,
    val height: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DecodedImage

        if (width != other.width) return false
        if (height != other.height) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + data.contentHashCode()
        return result
    }
}

/**
 * Decode an image from the given filesystem path into a tightly packed RGBA byte array.
 * Uses stb_image via Kotlin/Native cinterop.
 *
 * @return DecodedImage containing the image data, width, and height, or null on failure.
 */
fun readImageRGBA(path: String): DecodedImage? = memScoped {
    val w = alloc<IntVar>()
    val h = alloc<IntVar>()
    val ch = alloc<IntVar>()

    // Request RGBA (4 channels)
    val desiredChannels = 4

    val pixelsPtr = stbi_load(
        filename = path,
        x = w.ptr,
        y = h.ptr,
        channels_in_file = ch.ptr,
        desired_channels = desiredChannels
    ) ?: return@memScoped null

    try {
        val width = w.value
        val height = h.value
        if (width <= 0 || height <= 0) return@memScoped null

        val sizeLong = 1L * width * height * desiredChannels
        if (sizeLong > Int.MAX_VALUE) return@memScoped null
        val size = sizeLong.toInt()

        val out = ByteArray(size)
        out.usePinned { pinned ->
            memcpy(pinned.addressOf(0), pixelsPtr, size.convert())
        }
        DecodedImage(out, width, height)
    } finally {
        stbi_image_free(pixelsPtr)
    }
}
