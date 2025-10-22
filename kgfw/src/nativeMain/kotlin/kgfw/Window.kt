package kgfw

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import rgfw.RGFW_FALSE
import rgfw.RGFW_createWindow
import rgfw.RGFW_event
import rgfw.RGFW_isMousePressed
import rgfw.RGFW_m
import rgfw.RGFW_mouseButtonPressed
import rgfw.RGFW_point
import rgfw.RGFW_quit
import rgfw.RGFW_rect
import rgfw.RGFW_window
import rgfw.RGFW_window_checkEvent
import rgfw.RGFW_window_close
import rgfw.RGFW_window_getMousePoint
import rgfw.RGFW_window_shouldClose
import rgfw.RGFW_window_swapBuffers

fun window(
    name: String,
    width: Int,
    height: Int,
    onEvent: (event: Event) -> Unit = {},
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
        RGFW_window_checkEvent(windowPointer)?.let { event ->
            when (event.pointed.type.toUInt()) {
                RGFW_quit -> {
                    onEvent(Event.Quit)
                    break
                }
                RGFW_mouseButtonPressed -> {
                    memScoped {
                        RGFW_window_getMousePoint(windowPointer)
                            .getPointer(this)
                            .pointed
                            .let { mousePoint ->
                                onEvent(
                                    Event.MouseButtonPressed(
                                        x = mousePoint.x,
                                        y = mousePoint.y,
                                    )
                                )
                            }
                    }
                }
            }
        }
        block()
        RGFW_window_swapBuffers(windowPointer)
    }
    RGFW_window_close(windowPointer)
}
