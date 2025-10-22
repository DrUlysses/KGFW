package kgfw

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cValue
import rgfw.RGFW_FALSE
import rgfw.RGFW_createWindow
import rgfw.RGFW_rect
import rgfw.RGFW_window
import rgfw.RGFW_window_close
import rgfw.RGFW_window_shouldClose
import rgfw.RGFW_window_swapBuffers

fun window(
    name: String,
    width: Int,
    height: Int,
    block: () -> Unit
) {
    val windowPointer: CPointer<RGFW_window> = RGFW_createWindow(
        name = name,
        rect = cValue<RGFW_rect> {
            x = 0
            y = 0
            w = width
            h = height
        },
        flags = 0u
    ) ?: throw Exception("Failed to create kgfw.window.")

    while (RGFW_window_shouldClose(windowPointer) == RGFW_FALSE) {
        block()
        RGFW_window_swapBuffers(windowPointer)
    }
    RGFW_window_close(windowPointer)
}
