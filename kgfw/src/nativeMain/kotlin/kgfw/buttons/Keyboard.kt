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
