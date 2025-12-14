package kgfw.buttons

import rgfw.*

enum class Keyboard {
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
    Enter,

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
    F13,
    F14,
    F15,
    F16,
    F17,
    F18,
    F19,
    F20,
    F21,
    F22,
    F23,
    F24,
    F25,
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
    Menu,
    End,
    Home,
    PageUp,
    PageDown,
    NumLock,

    KpSlash,
    KpMultiply,
    KpPlus,
    KpMinus,
    KpEqual,
    Kp1,
    Kp2,
    Kp3,
    Kp4,
    Kp5,
    Kp6,
    Kp7,
    Kp8,
    Kp9,
    Kp0,
    KpPeriod,
    KpReturn,
    ScrollLock,
    PrintScreen,
    Pause,
    World1,
    World2,

    /**
     * Padding for alignment (RGFW 1.8.1 uses 256)
     */
    KeyLast,
    Unknown,
}

internal fun UInt.toKeyboard(): Keyboard = when (this) {
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
    RGFW_return -> Keyboard.Enter
    RGFW_enter -> Keyboard.Enter
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
    RGFW_F13 -> Keyboard.F13
    RGFW_F14 -> Keyboard.F14
    RGFW_F15 -> Keyboard.F15
    RGFW_F16 -> Keyboard.F16
    RGFW_F17 -> Keyboard.F17
    RGFW_F18 -> Keyboard.F18
    RGFW_F19 -> Keyboard.F19
    RGFW_F20 -> Keyboard.F20
    RGFW_F21 -> Keyboard.F21
    RGFW_F22 -> Keyboard.F22
    RGFW_F23 -> Keyboard.F23
    RGFW_F24 -> Keyboard.F24
    RGFW_F25 -> Keyboard.F25
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
    RGFW_menu -> Keyboard.Menu
    RGFW_end -> Keyboard.End
    RGFW_home -> Keyboard.Home
    RGFW_pageUp -> Keyboard.PageUp
    RGFW_pageDown -> Keyboard.PageDown
    RGFW_numLock -> Keyboard.NumLock
    RGFW_kpSlash -> Keyboard.KpSlash
    RGFW_kpMultiply -> Keyboard.KpMultiply
    RGFW_kpPlus -> Keyboard.KpPlus
    RGFW_kpMinus -> Keyboard.KpMinus
    RGFW_kpEqual -> Keyboard.KpEqual
    RGFW_kp1 -> Keyboard.Kp1
    RGFW_kp2 -> Keyboard.Kp2
    RGFW_kp3 -> Keyboard.Kp3
    RGFW_kp4 -> Keyboard.Kp4
    RGFW_kp5 -> Keyboard.Kp5
    RGFW_kp6 -> Keyboard.Kp6
    RGFW_kp7 -> Keyboard.Kp7
    RGFW_kp8 -> Keyboard.Kp8
    RGFW_kp9 -> Keyboard.Kp9
    RGFW_kp0 -> Keyboard.Kp0
    RGFW_kpPeriod -> Keyboard.KpPeriod
    RGFW_kpReturn -> Keyboard.KpReturn
    RGFW_scrollLock -> Keyboard.ScrollLock
    RGFW_printScreen -> Keyboard.PrintScreen
    RGFW_pause -> Keyboard.Pause
    RGFW_world1 -> Keyboard.World1
    RGFW_world2 -> Keyboard.World2
    RGFW_keyLast -> Keyboard.KeyLast
    else -> Keyboard.Unknown
}
