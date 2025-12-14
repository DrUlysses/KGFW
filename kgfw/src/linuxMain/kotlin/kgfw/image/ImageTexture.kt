package kgfw.image

import kotlinx.cinterop.*
import rgfw.*

class ImageTexture(
    imageData: ByteArray,
    val width: Int,
    val height: Int
) {
    val textureId = memScoped {
        val textures = allocArray<GLuintVar>(length = 1)
        glGenTextures(n = 1, textures = textures)
        textures[0]
    }

    init {
        glBindTexture(target = GL_TEXTURE_2D.toUInt(), texture = textureId)
        glTexParameteri(target = GL_TEXTURE_2D.toUInt(), pname = GL_TEXTURE_MIN_FILTER.toUInt(), param = GL_NEAREST)
        glTexParameteri(target = GL_TEXTURE_2D.toUInt(), pname = GL_TEXTURE_MAG_FILTER.toUInt(), param = GL_NEAREST)

        // Ensure byte-aligned rows (no padding) for RGBA byte arrays
        glPixelStorei(pname = GL_UNPACK_ALIGNMENT.toUInt(), param = 1)

        imageData.usePinned { pinnedData ->
            glTexImage2D(
                target = GL_TEXTURE_2D.toUInt(),
                level = 0,
                internalFormat = GL_RGBA,
                width = width,
                height = height,
                border = 0,
                format = GL_RGBA.toUInt(),
                type = GL_UNSIGNED_BYTE.toUInt(),
                pixels = pinnedData.addressOf(index = 0)
            )
        }
    }

    /**
     * Draw at native texture size
     */
    fun draw(
        win: CPointer<RGFW_window>,
        x: Int,
        y: Int
    ) {
        internalDraw(
            win = win,
            x = x,
            y = y,
            drawW = width,
            drawH = height
        )
    }

    /**
     * Draws this texture scaled to a certain size.
     * Keeps the top-left corner at (x, y) and stretches to (w, h).
     */
    fun drawScaled(
        win: CPointer<RGFW_window>,
        x: Int,
        y: Int,
        h: Int,
        w: Int
    ) {
        internalDraw(
            win = win,
            x = x,
            y = y,
            drawW = w,
            drawH = h
        )
    }

    private fun internalDraw(
        win: CPointer<RGFW_window>,
        x: Int,
        y: Int,
        drawW: Int,
        drawH: Int
    ) {
        val (windowWidth, windowHeight) = memScoped {
            val wVar = alloc<IntVar>()
            val hVar = alloc<IntVar>()
            RGFW_window_getSize(win = win, w = wVar.ptr, h = hVar.ptr)
            wVar.value to hVar.value
        }

        // Save current projection
        glMatrixMode(GL_PROJECTION.toUInt())
        glPushMatrix()
        glLoadIdentity()
        glOrtho(
            left = 0.0,
            right = windowWidth.toDouble(),
            bottom = windowHeight.toDouble(),
            top = 0.0,
            near_val = -1.0,
            far_val = 1.0
        )

        // Save current modelview
        glMatrixMode(GL_MODELVIEW.toUInt())
        glPushMatrix()
        glLoadIdentity()

        glEnable(cap = GL_TEXTURE_2D.toUInt())
        glEnable(cap = GL_BLEND.toUInt())
        glBlendFunc(sfactor = GL_SRC_ALPHA.toUInt(), dfactor = GL_ONE_MINUS_SRC_ALPHA.toUInt())
        glColor4f(red = 1f, green = 1f, blue = 1f, alpha = 1f)

        glBindTexture(target = GL_TEXTURE_2D.toUInt(), texture = textureId)
        glBegin(mode = GL_QUADS.toUInt())
        glTexCoord2f(s = 0f, t = 0f); glVertex2i(x = x, y = y)
        glTexCoord2f(s = 1f, t = 0f); glVertex2i(x = x + drawW, y = y)
        glTexCoord2f(s = 1f, t = 1f); glVertex2i(x = x + drawW, y = y + drawH)
        glTexCoord2f(s = 0f, t = 1f); glVertex2i(x = x, y = y + drawH)
        glEnd()

        glDisable(cap = GL_TEXTURE_2D.toUInt())
        glDisable(cap = GL_BLEND.toUInt())

        // Restore matrices
        glPopMatrix() // modelview
        glMatrixMode(GL_PROJECTION.toUInt())
        glPopMatrix()
        glMatrixMode(GL_MODELVIEW.toUInt())
    }

    fun dispose() {
        memScoped {
            val textures = allocArray<GLuintVar>(length = 1)
            textures[0] = textureId
            glDeleteTextures(n = 1, textures = textures)
        }
    }
}
