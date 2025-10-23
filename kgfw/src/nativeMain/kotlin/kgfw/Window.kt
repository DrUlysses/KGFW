package kgfw

import kgfw.buttons.Gamepad
import kgfw.buttons.Keyboard
import kgfw.buttons.Mouse
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
    block: () -> Unit
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
                    RGFW_window_close(windowPointer)
                    return@memScoped
                }
            }
        }
        block()
        RGFW_window_swapBuffers(windowPointer)
    }
    RGFW_window_close(windowPointer)
}

private fun UInt.toKeyboard(): Keyboard = when (this) {
    RGFW_keyNULL -> Keyboard.KeyNULL
    RGFW_escape -> Keyboard.Escape
    RGFW_backtick -> Keyboard.Backtick
    RGFW_0 -> Keyboard.Zero
    RGFW_1 -> Keyboard.One
    RGFW_2 -> Keyboard.Two
    RGFW_3 -> Keyboard.Three
    RGFW_4 -> Keyboard.Four
    RGFW_5 -> Keyboard.Five
    RGFW_6 -> Keyboard.Six
    RGFW_7 -> Keyboard.Seven
    RGFW_8 -> Keyboard.Eight
    RGFW_9 -> Keyboard.Nine
    RGFW_minus -> Keyboard.Minus
    RGFW_equals -> Keyboard.Equals
    RGFW_backSpace -> Keyboard.BackSpace
    RGFW_tab -> Keyboard.Tab
    RGFW_space -> Keyboard.Space
    RGFW_a -> Keyboard.A
    RGFW_b -> Keyboard.B
    RGFW_c -> Keyboard.C
    RGFW_d -> Keyboard.D
    RGFW_e -> Keyboard.E
    RGFW_f -> Keyboard.F
    RGFW_g -> Keyboard.G
    RGFW_h -> Keyboard.H
    RGFW_i -> Keyboard.I
    RGFW_j -> Keyboard.J
    RGFW_k -> Keyboard.K
    RGFW_l -> Keyboard.L
    RGFW_m -> Keyboard.M
    RGFW_n -> Keyboard.N
    RGFW_o -> Keyboard.O
    RGFW_p -> Keyboard.P
    RGFW_q -> Keyboard.Q
    RGFW_r -> Keyboard.R
    RGFW_s -> Keyboard.S
    RGFW_t -> Keyboard.T
    RGFW_u -> Keyboard.U
    RGFW_v -> Keyboard.V
    RGFW_w -> Keyboard.W
    RGFW_x -> Keyboard.X
    RGFW_y -> Keyboard.Y
    RGFW_z -> Keyboard.Z
    RGFW_period -> Keyboard.Period
    RGFW_comma -> Keyboard.Comma
    RGFW_slash -> Keyboard.Slash
    RGFW_bracket -> Keyboard.Bracket
    RGFW_closeBracket -> Keyboard.CloseBracket
    RGFW_semicolon -> Keyboard.Semicolon
    RGFW_apostrophe -> Keyboard.Apostrophe
    RGFW_backSlash -> Keyboard.BackSlash
    RGFW_return -> Keyboard.Return
    RGFW_delete -> Keyboard.Delete
    RGFW_F1 -> Keyboard.F1
    RGFW_F2 -> Keyboard.F2
    RGFW_F3 -> Keyboard.F3
    RGFW_F4 -> Keyboard.F4
    RGFW_F5 -> Keyboard.F5
    RGFW_F6 -> Keyboard.F6
    RGFW_F7 -> Keyboard.F7
    RGFW_F8 -> Keyboard.F8
    RGFW_F9 -> Keyboard.F9
    RGFW_F10 -> Keyboard.F10
    RGFW_F11 -> Keyboard.F11
    RGFW_F12 -> Keyboard.F12
    RGFW_capsLock -> Keyboard.CapsLock
    RGFW_shiftL -> Keyboard.ShiftL
    RGFW_controlL -> Keyboard.ControlL
    RGFW_altL -> Keyboard.AltL
    RGFW_superL -> Keyboard.SuperL
    RGFW_shiftR -> Keyboard.ShiftR
    RGFW_controlR -> Keyboard.ControlR
    RGFW_altR -> Keyboard.AltR
    RGFW_superR -> Keyboard.SuperR
    RGFW_up -> Keyboard.Up
    RGFW_down -> Keyboard.Down
    RGFW_left -> Keyboard.Left
    RGFW_right -> Keyboard.Right
    RGFW_insert -> Keyboard.Insert
    RGFW_end -> Keyboard.End
    RGFW_home -> Keyboard.Home
    RGFW_pageUp -> Keyboard.PageUp
    RGFW_pageDown -> Keyboard.PageDown
    RGFW_numLock -> Keyboard.NumLock
    RGFW_scrollLock -> Keyboard.ScrollLock
    RGFW_keyLast -> Keyboard.KeyLast
    else -> Keyboard.Unknown
}

private fun UInt.toMouse(): Mouse = when (this) {
    RGFW_mouseLeft -> Mouse.MouseLeft
    RGFW_mouseRight -> Mouse.MouseRight
    RGFW_mouseMiddle -> Mouse.MouseMiddle
    RGFW_mouseMisc1 -> Mouse.MouseMisc1
    RGFW_mouseMisc2 -> Mouse.MouseMisc2
    RGFW_mouseMisc3 -> Mouse.MouseMisc3
    RGFW_mouseMisc4 -> Mouse.MouseMisc4
    RGFW_mouseMisc5 -> Mouse.MouseMisc5
    RGFW_mouseFinal -> Mouse.MouseFinal
    else -> Mouse.Unknown
}

private fun UInt.toGamepad(): Gamepad = when (this) {
    RGFW_gamepadNone -> Gamepad.GamepadNone
    RGFW_gamepadA -> Gamepad.GamepadA
    RGFW_gamepadB -> Gamepad.GamepadB
    RGFW_gamepadY -> Gamepad.GamepadY
    RGFW_gamepadX -> Gamepad.GamepadX
    RGFW_gamepadStart -> Gamepad.GamepadStart
    RGFW_gamepadSelect -> Gamepad.GamepadSelect
    RGFW_gamepadHome -> Gamepad.GamepadHome
    RGFW_gamepadUp -> Gamepad.GamepadUp
    RGFW_gamepadDown -> Gamepad.GamepadDown
    RGFW_gamepadLeft -> Gamepad.GamepadLeft
    RGFW_gamepadRight -> Gamepad.GamepadRight
    RGFW_gamepadL1 -> Gamepad.GamepadL1
    RGFW_gamepadL2 -> Gamepad.GamepadL2
    RGFW_gamepadR1 -> Gamepad.GamepadR1
    RGFW_gamepadR2 -> Gamepad.GamepadR2
    RGFW_gamepadL3 -> Gamepad.GamepadL3
    RGFW_gamepadR3 -> Gamepad.GamepadR3
    RGFW_gamepadFinal -> Gamepad.GamepadFinal
    else -> Gamepad.Unknown
}
