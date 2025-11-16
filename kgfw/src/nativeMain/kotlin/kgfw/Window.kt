package kgfw

import kgfw.buttons.toGamepad
import kgfw.buttons.toKeyboard
import kgfw.buttons.toMouse
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
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
        rect = cValue<RGFW_rect> {
            x = 0
            y = 0
            w = width
            h = height
        },
        flags = 0u
    ) ?: throw Exception("Failed to create kgfw.window.")

    while (RGFW_window_shouldClose(windowPointer) == RGFW_FALSE) {
        // Process ALL pending events before rendering
        while (true) {
            val event = RGFW_window_checkEvent(windowPointer) ?: break

            when (event.pointed.type.toUInt()) {
                RGFW_keyPressed -> {
                    onEvent(
                        Event.KeyPressed(
                            button = event.pointed.key.toUInt().toKeyboard(),
                        )
                    )
                }
                RGFW_keyReleased -> {
                    onEvent(
                        Event.KeyReleased(
                            button = event.pointed.key.toUInt().toKeyboard(),
                        )
                    )
                }
                RGFW_mouseButtonPressed -> {
                    val mousePoint = RGFW_window_getMousePoint(windowPointer)
                        .getPointer(this@memScoped)
                        .pointed

                    onEvent(
                        Event.MouseButtonPressed(
                            x = mousePoint.x,
                            y = mousePoint.y,
                            button = event.pointed.button.toUInt().toMouse(),
                        )
                    )
                }
                RGFW_mouseButtonReleased -> {
                    val mousePoint = RGFW_window_getMousePoint(windowPointer)
                        .getPointer(this@memScoped)
                        .pointed

                    onEvent(
                        Event.MouseButtonReleased(
                            x = mousePoint.x,
                            y = mousePoint.y,
                            button = event.pointed.button.toUInt().toMouse(),
                        )
                    )
                }

                RGFW_mousePosChanged -> {
                    val mousePoint = RGFW_window_getMousePoint(windowPointer)
                        .getPointer(this@memScoped)
                        .pointed

                    onEvent(
                        Event.MousePosChanged(
                            x = mousePoint.x,
                            y = mousePoint.y,
                        )
                    )
                }

                RGFW_gamepadConnected -> {
                    onEvent(
                        Event.GamepadConnected(
                            gamepad = event.pointed.gamepad.toInt()
                        )
                    )
                }

                RGFW_gamepadDisconnected -> {
                    onEvent(
                        Event.GamepadDisconnected(
                            gamepad = event.pointed.gamepad.toInt()
                        )
                    )
                }

                RGFW_gamepadButtonPressed -> {
                    onEvent(
                        Event.GamepadButtonPressed(
                            gamepad = event.pointed.gamepad.toInt(),
                            button = event.pointed.button.toUInt().toGamepad()
                        )
                    )
                }

                RGFW_gamepadButtonReleased -> {
                    onEvent(
                        Event.GamepadButtonReleased(
                            gamepad = event.pointed.gamepad.toInt(),
                            button = event.pointed.button.toUInt().toGamepad()
                        )
                    )
                }

                RGFW_gamepadAxisMove -> {
                    onEvent(
                        Event.GamepadAxisMove(
                            gamepad = event.pointed.gamepad.toInt(),
                            whichAxis = event.pointed.whichAxis.toInt(),
                            axesCount = event.pointed.axisesCount.toInt()
                        )
                    )
                }

                RGFW_windowMoved -> {
                    onEvent(Event.WindowMoved)
                }

                RGFW_windowResized -> {
                    onEvent(Event.WindowResized)
                }

                RGFW_focusIn -> {
                    onEvent(Event.FocusIn)
                }

                RGFW_focusOut -> {
                    onEvent(Event.FocusOut)
                }

                RGFW_mouseEnter -> {
                    onEvent(Event.MouseEnter)
                }

                RGFW_mouseLeave -> {
                    onEvent(Event.MouseLeave)
                }

                RGFW_windowRefresh -> {
                    onEvent(Event.WindowRefresh)
                }

                RGFW_DNDInit -> {
                    val p = event.pointed.point
                    onEvent(Event.DNDInit(x = p.x, y = p.y))
                }

                RGFW_DND -> {
                    val p = event.pointed.point
                    // NOTE: Dropped files list (char**) interop can be added later if needed.
                    // For now, emit the event with a position and an empty file list.
                    onEvent(Event.DND(x = p.x, y = p.y, files = emptyList()))
                }

                RGFW_windowMaximized -> {
                    onEvent(Event.WindowMaximized)
                }

                RGFW_windowMinimized -> {
                    onEvent(Event.WindowMinimized)
                }

                RGFW_windowRestored -> {
                    onEvent(Event.WindowRestored)
                }

                RGFW_scaleUpdated -> {
                    onEvent(
                        Event.ScaleUpdated(
                            scaleX = event.pointed.scaleX,
                            scaleY = event.pointed.scaleY
                        )
                    )
                }

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
        glFlush()
    }
    onDispose(windowPointer)
    RGFW_window_close(windowPointer)
}
