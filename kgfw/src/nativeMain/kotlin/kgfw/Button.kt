package kgfw

enum class Button {
    // Keyboard
    KeyNULL,
    Escape,
    Backtick,
    Zero,
    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Minus,
    Equals,
    BackSpace,
    Tab,
    Space,
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J,
    K,
    L,
    M,
    N,
    O,
    P,
    Q,
    R,
    S,
    T,
    U,
    V,
    W,
    X,
    Y,
    Z,
    Period,
    Comma,
    Slash,
    Bracket,
    CloseBracket,
    Semicolon,
    Apostrophe,
    BackSlash,
    Return,

    /**
     * 127
     */
    Delete,
    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12,
    CapsLock,
    ShiftL,
    ControlL,
    AltL,
    SuperL,
    ShiftR,
    ControlR,
    AltR,
    SuperR,
    Up,
    Down,
    Left,
    Right,
    Insert,
    End,
    Home,
    PageUp,
    PageDown,
    NumLock,
    ScrollLock,

    /**
     * padding for alignment ~(175 by default)
     */
    KeyLast,
    // Mouse
    MouseLeft,
    MouseRight,
    MouseMiddle,
    MouseMisc1,
    MouseMisc2,
    MouseMisc3,
    MouseMisc4,
    MouseMisc5,
    MouseFinal,

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
    // Other
    Unknown,
}
