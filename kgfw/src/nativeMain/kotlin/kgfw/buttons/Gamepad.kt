package kgfw.buttons

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
