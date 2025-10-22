package kgfw

sealed interface Event {
    data class KeyPressed(val button: Button) : Event
    data class KeyReleased(val button: Button) : Event
    data class MouseButtonPressed(val x: Int, val y: Int, val button: Button) : Event
    data class MouseButtonReleased(val x: Int, val y: Int, val button: Button) : Event
    data object Quit : Event
}
