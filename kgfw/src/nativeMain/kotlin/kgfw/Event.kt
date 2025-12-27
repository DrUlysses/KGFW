package kgfw

import kgfw.buttons.Keyboard
import kgfw.buttons.Mouse

sealed interface Event {
    // Keyboard
    data class KeyPressed(val button: Keyboard) : Event
    data class KeyReleased(val button: Keyboard) : Event

    // Mouse
    data class MouseButtonPressed(val x: Int, val y: Int, val button: Mouse) : Event
    data class MouseButtonReleased(val x: Int, val y: Int, val button: Mouse) : Event
    data class MousePosChanged(val x: Int, val y: Int) : Event
    data class MouseScroll(val x: Float, val y: Float) : Event
    data object MouseEnter : Event
    data object MouseLeave : Event

    // Window and focus
    data object FocusIn : Event
    data object FocusOut : Event
    data object WindowRefresh : Event
    data object WindowMoved : Event
    data class WindowResized(val width: Int, val height: Int) : Event
    data class WindowMaximized(val width: Int, val height: Int) : Event
    data object WindowMinimized : Event
    data class WindowRestored(val width: Int, val height: Int) : Event
    data class ScaleUpdated(val scaleX: Float, val scaleY: Float) : Event

    // Drag and drop
    data class DNDInit(val x: Int, val y: Int) : Event
    data class DND(val x: Int, val y: Int, val files: List<String>) : Event

    // Quit
    data object Quit : Event
}
