package kgfw.buttons

import rgfw.*

enum class Gamepad {
    // Gamepad
    GamepadNone,

    /**
     * !< or PS X button
     */
    GamepadA,

    /**
     * !< or PS circle button
     */
    GamepadB,

    /**
     * !< or PS triangle button
     */
    GamepadY,

    /**
     * !< or PS square button
     */
    GamepadX,

    /**
     * !< start button
     */
    GamepadStart,

    /**
     * !< select button
     */
    GamepadSelect,

    /**
     * !< home button
     */
    GamepadHome,

    /**
     * !< dpad up
     */
    GamepadUp,

    /**
     * !< dpad down
     */
    GamepadDown,

    /**
     * !< dpad left
     */
    GamepadLeft,

    /**
     * !< dpad right
     */
    GamepadRight,

    /**
     * !< left bump
     */
    GamepadL1,

    /**
     * !< left trigger
     */
    GamepadL2,

    /**
     * !< right bumper
     */
    GamepadR1,

    /**
     * !< right trigger
     */
    GamepadR2,

    /**
     * left thumb stick
     */
    GamepadL3,

    /**
     * !< right thumb stick
     */
    GamepadR3,
    GamepadFinal,
    Unknown,
}

internal fun UInt.toGamepad(): Gamepad = when (this) {
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
