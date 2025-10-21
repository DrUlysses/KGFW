package dr.ulysses

import kotlinx.cinterop.CPointer
import rgfw.RGFW_createWindow
import rgfw.RGFW_rect
import rgfw.RGFW_window

class Window(
    val name: String,
    val width: Int,
    val height: Int
) {
    val pointer: CPointer<RGFW_window> = RGFW_createWindow(
        name = name,
        rect = RGFW_rect(0, 0, width, height),
        flags = 0u
    ) ?: throw Exception("Failed to create window.")
}
