package kgfw

sealed interface Event {
    data object Quit : Event
    data class MouseButtonPressed(val x: Int, val y: Int) : Event
}
