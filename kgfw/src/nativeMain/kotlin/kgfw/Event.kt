package kgfw

sealed interface Event {
    // Keyboard
    data class KeyPressed(val button: Button) : Event
    data class KeyReleased(val button: Button) : Event

    // Mouse
    data class MouseButtonPressed(val x: Int, val y: Int, val button: Button) : Event
    data class MouseButtonReleased(val x: Int, val y: Int, val button: Button) : Event
    data class MousePosChanged(val x: Int, val y: Int) : Event
    data object MouseEnter : Event
    data object MouseLeave : Event

    // Window and focus
    data object FocusIn : Event
    data object FocusOut : Event
    data object WindowRefresh : Event
    data object WindowMoved : Event
    data object WindowResized : Event
    data object WindowMaximized : Event
    data object WindowMinimized : Event
    data object WindowRestored : Event
    data class ScaleUpdated(val scaleX: Float, val scaleY: Float) : Event

    // Gamepad
    data class GamepadConnected(val gamepad: Int) : Event
    data class GamepadDisconnected(val gamepad: Int) : Event
    data class GamepadButtonPressed(val gamepad: Int, val button: Button) : Event
    data class GamepadButtonReleased(val gamepad: Int, val button: Button) : Event
    data class GamepadAxisMove(val gamepad: Int, val whichAxis: Int, val axesCount: Int) : Event

    // Drag and drop
    data class DNDInit(val x: Int, val y: Int) : Event
    data class DND(val x: Int, val y: Int, val files: List<String>) : Event

    // Quit
    data object Quit : Event
}
