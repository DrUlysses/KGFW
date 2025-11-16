package kgfw.buttons

import rgfw.*

enum class Mouse {
    MouseLeft,
    MouseRight,
    MouseMiddle,
    MouseMisc1,
    MouseMisc2,
    MouseMisc3,
    MouseMisc4,
    MouseMisc5,
    MouseFinal,
    Unknown,
}

internal fun UInt.toMouse(): Mouse = when (this) {
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
