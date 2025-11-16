package kgfw.image

import kotlinx.cinterop.*
import platform.opengl32.*
import rgfw.*

class ImageTexture(
    imageData: ByteArray,
    val width: Int,
    val height: Int
) {
    val textureId = memScoped {
        val textures = allocArray<GLuintVar>(1)
        glGenTextures(1, textures)
        textures[0]
    }

    init {
        glBindTexture(GL_TEXTURE_2D.toUInt(), textureId)
        glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_MIN_FILTER.toUInt(), GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_MAG_FILTER.toUInt(), GL_NEAREST)

        // Ensure byte-aligned rows (no padding) for RGBA byte arrays
        glPixelStorei(GL_UNPACK_ALIGNMENT.toUInt(), 1)

        imageData.usePinned { pinnedData ->
            glTexImage2D(
                GL_TEXTURE_2D.toUInt(),
                0,
                GL_RGBA,
                width,
                height,
                0,
                GL_RGBA.toUInt(),
                GL_UNSIGNED_BYTE.toUInt(),
                pinnedData.addressOf(0)
            )
        }
    }

    /**
     * Draw at native texture size
     */
    fun draw(win: CPointer<RGFW_window>, x: Int, y: Int) {
        internalDraw(win, x, y, width, height)
    }

    /**
     * Draws this texture scaled to a certain size.
     * Keeps the top-left corner at (x, y) and stretches to (w, h).
     */
    fun drawScaled(win: CPointer<RGFW_window>, x: Int, y: Int, h: Int, w: Int) {
        internalDraw(win, x, y, w, h)
    }

    private fun internalDraw(win: CPointer<RGFW_window>, x: Int, y: Int, drawW: Int, drawH: Int) {
        val windowWidth = win.pointed.r.w
        val windowHeight = win.pointed.r.h

        // Save current projection
        glMatrixMode(GL_PROJECTION.toUInt())
        glPushMatrix()
        glLoadIdentity()
        glOrtho(0.0, windowWidth.toDouble(), windowHeight.toDouble(), 0.0, -1.0, 1.0)

        // Save current modelview
        glMatrixMode(GL_MODELVIEW.toUInt())
        glPushMatrix()
        glLoadIdentity()

        glEnable(GL_TEXTURE_2D.toUInt())
        glEnable(GL_BLEND.toUInt())
        glBlendFunc(GL_SRC_ALPHA.toUInt(), GL_ONE_MINUS_SRC_ALPHA.toUInt())
        glColor4f(1f, 1f, 1f, 1f)

        glBindTexture(GL_TEXTURE_2D.toUInt(), textureId)
        glBegin(GL_QUADS.toUInt())
        glTexCoord2f(0f, 0f); glVertex2i(x, y)
        glTexCoord2f(1f, 0f); glVertex2i(x + drawW, y)
        glTexCoord2f(1f, 1f); glVertex2i(x + drawW, y + drawH)
        glTexCoord2f(0f, 1f); glVertex2i(x, y + drawH)
        glEnd()

        glDisable(GL_TEXTURE_2D.toUInt())
        glDisable(GL_BLEND.toUInt())

        // Restore matrices
        glPopMatrix() // modelview
        glMatrixMode(GL_PROJECTION.toUInt())
        glPopMatrix()
        glMatrixMode(GL_MODELVIEW.toUInt())
    }

    fun dispose() {
        memScoped {
            val textures = allocArray<GLuintVar>(1)
            textures[0] = textureId
            glDeleteTextures(1, textures)
        }
    }
}
