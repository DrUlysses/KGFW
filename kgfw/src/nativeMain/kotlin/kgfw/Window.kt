package kgfw

import kgfw.buttons.toKeyboard
import kgfw.buttons.toMouse
import kotlinx.cinterop.*
import kotlinx.cinterop.nativeHeap.alloc
import rgfw.*

fun window(
    name: String,
    width: Int,
    height: Int,
    onEvent: (event: Event) -> Unit = {},
    onDispose: (windowPointer: CPointer<RGFW_window>) -> Unit = {},
    block: (windowPointer: CPointer<RGFW_window>) -> Unit
) = memScoped {
    val windowPointer: CPointer<RGFW_window> = RGFW_createWindow(
        name = name,
        x = 0,
        y = 0,
        w = width,
        h = height,
        flags = (RGFW_windowOpenGL or RGFW_windowCenter or RGFW_windowAllowDND)
    ) ?: throw Exception("Failed to create kgfw.window.")

    while (RGFW_window_shouldClose(windowPointer) == RGFW_FALSE) {
        // Process ALL pending events before rendering
        val event = alloc<RGFW_event>()
        while (RGFW_window_checkEvent(windowPointer, event.ptr) == RGFW_TRUE) {
            when (event.type.toUInt()) {
                RGFW_keyPressed ->
                    onEvent(
                        Event.KeyPressed(
                            button = event.key.value.toUInt().toKeyboard()
                        )
                    )

                RGFW_keyReleased ->
                    onEvent(
                        Event.KeyReleased(
                            button = event.key.value.toUInt().toKeyboard()
                        )
                    )

                RGFW_mouseButtonPressed -> {
                    val (x, y) = getMousePos(windowPointer)
                    onEvent(
                        Event.MouseButtonPressed(
                            x = x,
                            y = y,
                            button = event.button.value.toUInt().toMouse(),
                        )
                    )
                }

                RGFW_mouseButtonReleased -> {
                    val (x, y) = getMousePos(windowPointer)
                    onEvent(
                        Event.MouseButtonReleased(
                            x = x,
                            y = y,
                            button = event.button.value.toUInt().toMouse(),
                        )
                    )
                }

                RGFW_mousePosChanged -> {
                    onEvent(
                        Event.MousePosChanged(
                            x = event.mouse.x,
                            y = event.mouse.y
                        )
                    )
                }

                RGFW_mouseScroll -> {
                    onEvent(
                        Event.MouseScroll(
                            x = event.scroll.x,
                            y = event.scroll.y
                        )
                    )
                }

                RGFW_windowMoved ->
                    onEvent(
                        Event.WindowMoved
                    )

                RGFW_windowResized ->
                    onEvent(
                        Event.WindowResized(
                            width = windowPointer.pointed.w,
                            height = windowPointer.pointed.h
                        )
                    )

                RGFW_focusIn ->
                    onEvent(
                        Event.FocusIn
                    )

                RGFW_focusOut ->
                    onEvent(
                        Event.FocusOut
                    )

                RGFW_mouseEnter ->
                    onEvent(
                        Event.MouseEnter
                    )

                RGFW_mouseLeave ->
                    onEvent(
                        Event.MouseLeave
                    )

                RGFW_windowRefresh ->
                    onEvent(
                        Event.WindowRefresh
                    )

                RGFW_dataDrag -> {
                    onEvent(
                        Event.DNDInit(
                            x = event.drag.x,
                            y = event.drag.y
                        )
                    )
                }

                RGFW_dataDrop -> {
                    val (x, y) = getMousePos(windowPointer)
                    val files = event.drop.toFileList()
                    onEvent(Event.DND(x = x, y = y, files = files))
                }

                RGFW_windowMaximized ->
                    onEvent(
                        Event.WindowMaximized(
                            width = windowPointer.pointed.w,
                            height = windowPointer.pointed.h
                        )
                    )

                RGFW_windowMinimized ->
                    onEvent(
                        Event.WindowMinimized
                    )

                RGFW_windowRestored ->
                    onEvent(
                        Event.WindowRestored(
                            width = windowPointer.pointed.w,
                            height = windowPointer.pointed.h
                        )
                    )

                RGFW_scaleUpdated ->
                    onEvent(
                        Event.ScaleUpdated(
                            scaleX = event.scale.x,
                            scaleY = event.scale.y
                        )
                    )

                RGFW_quit -> {
                    onEvent(Event.Quit)
                    onDispose(windowPointer)
                    RGFW_window_close(windowPointer)
                    return@memScoped
                }
            }
        }
        block(windowPointer)
        RGFW_window_swapBuffers_OpenGL(windowPointer)
    }
    onDispose(windowPointer)
    RGFW_window_close(windowPointer)
}

/**
 * @return X to Y
 */
private fun getMousePos(
    windowPointer: CPointer<RGFW_window>
): Pair<Int, Int> = memScoped {
    val xVar = alloc<IntVar>()
    val yVar = alloc<IntVar>()
    return if (
        RGFW_window_getMouse(
            win = windowPointer,
            x = xVar.ptr,
            y = yVar.ptr
        ) == RGFW_TRUE
    ) {
        xVar.value to yVar.value
    } else {
        0 to 0
    }
}
