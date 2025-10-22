package kgfw

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
                    onEvent(Event.KeyPressed(
                        button = event.pointed.key.toUInt().toButton(),
                    ))
                }
                RGFW_keyReleased -> {
                    onEvent(Event.KeyReleased(
                        button = event.pointed.key.toUInt().toButton(),
                    ))
                }
                RGFW_quit -> {
                    onEvent(Event.Quit)
                    RGFW_window_close(windowPointer)
                    return@memScoped
                }
                RGFW_mouseButtonPressed -> {
                    val mousePoint = RGFW_window_getMousePoint(windowPointer)
                        .getPointer(this@memScoped)
                        .pointed

                    val mouseButton = event.pointed.button.toUInt().toButton()

                    onEvent(
                        Event.MouseButtonPressed(
                            x = mousePoint.x,
                            y = mousePoint.y,
                            button = mouseButton,
                        )
                    )
                }
                RGFW_mouseButtonReleased -> {
                    val mousePoint = RGFW_window_getMousePoint(windowPointer)
                        .getPointer(this@memScoped)
                        .pointed

                    val mouseButton = event.pointed.button.toUInt().toButton()

                    onEvent(
                        Event.MouseButtonReleased(
                            x = mousePoint.x,
                            y = mousePoint.y,
                            button = mouseButton,
                        )
                    )
                }
            }
        }
        block()
        RGFW_window_swapBuffers(windowPointer)
    }
    RGFW_window_close(windowPointer)
}

private fun UInt.toButton(): Button = when (this) {
    // Keyboard buttons
    RGFW_keyNULL -> Button.KeyNULL
    RGFW_escape -> Button.Escape
    RGFW_backtick -> Button.Backtick
    RGFW_0 -> Button.Zero
    RGFW_1 -> Button.One
    RGFW_2 -> Button.Two
    RGFW_3 -> Button.Three
    RGFW_4 -> Button.Four
    RGFW_5 -> Button.Five
    RGFW_6 -> Button.Six
    RGFW_7 -> Button.Seven
    RGFW_8 -> Button.Eight
    RGFW_9 -> Button.Nine
    RGFW_minus -> Button.Minus
    RGFW_equals -> Button.Equals
    RGFW_backSpace -> Button.BackSpace
    RGFW_tab -> Button.Tab
    RGFW_space -> Button.Space
    RGFW_a -> Button.A
    RGFW_b -> Button.B
    RGFW_c -> Button.C
    RGFW_d -> Button.D
    RGFW_e -> Button.E
    RGFW_f -> Button.F
    RGFW_g -> Button.G
    RGFW_h -> Button.H
    RGFW_i -> Button.I
    RGFW_j -> Button.J
    RGFW_k -> Button.K
    RGFW_l -> Button.L
    RGFW_m -> Button.M
    RGFW_n -> Button.N
    RGFW_o -> Button.O
    RGFW_p -> Button.P
    RGFW_q -> Button.Q
    RGFW_r -> Button.R
    RGFW_s -> Button.S
    RGFW_t -> Button.T
    RGFW_u -> Button.U
    RGFW_v -> Button.V
    RGFW_w -> Button.W
    RGFW_x -> Button.X
    RGFW_y -> Button.Y
    RGFW_z -> Button.Z
    RGFW_period -> Button.Period
    RGFW_comma -> Button.Comma
    RGFW_slash -> Button.Slash
    RGFW_bracket -> Button.Bracket
    RGFW_closeBracket -> Button.CloseBracket
    RGFW_semicolon -> Button.Semicolon
    RGFW_apostrophe -> Button.Apostrophe
    RGFW_backSlash -> Button.BackSlash
    RGFW_return -> Button.Return
    RGFW_delete -> Button.Delete
    RGFW_F1 -> Button.F1
    RGFW_F2 -> Button.F2
    RGFW_F3 -> Button.F3
    RGFW_F4 -> Button.F4
    RGFW_F5 -> Button.F5
    RGFW_F6 -> Button.F6
    RGFW_F7 -> Button.F7
    RGFW_F8 -> Button.F8
    RGFW_F9 -> Button.F9
    RGFW_F10 -> Button.F10
    RGFW_F11 -> Button.F11
    RGFW_F12 -> Button.F12
    RGFW_capsLock -> Button.CapsLock
    RGFW_shiftL -> Button.ShiftL
    RGFW_controlL -> Button.ControlL
    RGFW_altL -> Button.AltL
    RGFW_superL -> Button.SuperL
    RGFW_shiftR -> Button.ShiftR
    RGFW_controlR -> Button.ControlR
    RGFW_altR -> Button.AltR
    RGFW_superR -> Button.SuperR
    RGFW_up -> Button.Up
    RGFW_down -> Button.Down
    RGFW_left -> Button.Left
    RGFW_right -> Button.Right
    RGFW_insert -> Button.Insert
    RGFW_end -> Button.End
    RGFW_home -> Button.Home
    RGFW_pageUp -> Button.PageUp
    RGFW_pageDown -> Button.PageDown
    RGFW_numLock -> Button.NumLock
    RGFW_scrollLock -> Button.ScrollLock
    RGFW_keyLast -> Button.KeyLast
    // Mouse buttons
    RGFW_mouseLeft -> Button.MouseLeft
    RGFW_mouseRight -> Button.MouseRight
    RGFW_mouseMiddle -> Button.MouseMiddle
    RGFW_mouseMisc1 -> Button.MouseMisc1
    RGFW_mouseMisc2 -> Button.MouseMisc2
    RGFW_mouseMisc3 -> Button.MouseMisc3
    RGFW_mouseMisc4 -> Button.MouseMisc4
    RGFW_mouseMisc5 -> Button.MouseMisc5
    RGFW_mouseFinal -> Button.MouseFinal
    else -> Button.Unknown
}
