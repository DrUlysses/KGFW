import examples.texture
import examples.triangle
import kgfw.Event
import kgfw.buttons.Keyboard
import kgfw.window
import kotlinx.cinterop.CPointer
import rgfw.RGFW_window
import rgfw.RGFW_window_setName
import kotlin.time.Clock

enum class Example {
    Triangle,
    Texture
}

fun main() {
    val startTime = Clock.System.now().toEpochMilliseconds()
    var mouseX = 0
    var mouseY = 0
    val windowWidth = 800
    val windowHeight = 600
    var currentExample = Example.Triangle
    var window: CPointer<RGFW_window>? = null
    var onDispose: (windowPointer: CPointer<RGFW_window>) -> Unit = {}

    window(
        name = "Example ${currentExample.name}",
        width = windowWidth,
        height = windowHeight,
        onEvent = { event ->
            when (event) {
                is Event.Quit -> {
                    println("Quit")
                }

                is Event.MouseButtonPressed -> {
                    println("Mouse ${event.button} pressed at (${event.x}, ${event.y})")
                }

                is Event.MouseButtonReleased -> {
                    println("Mouse ${event.button} released at (${event.x}, ${event.y})")
                }

                is Event.MousePosChanged -> {
                    mouseX = event.x
                    mouseY = event.y
                }

                is Event.KeyPressed -> {
                    println("Key ${event.button} pressed")
                    when (event.button) {
                        Keyboard.F1 if currentExample != Example.Triangle -> {
                            window?.let {
                                onDispose(it)
                            }
                            currentExample = Example.Triangle
                            RGFW_window_setName(window, "Example ${currentExample.name}")
                        }

                        Keyboard.F2 if currentExample != Example.Texture -> {
                            window?.let {
                                onDispose(it)
                            }
                            currentExample = Example.Texture
                            RGFW_window_setName(window, "Example ${currentExample.name}")
                        }

                        else -> {}
                    }
                }

                is Event.KeyReleased -> {
                    println("Key ${event.button} released")
                }

                else -> {}
            }
        },
        onDispose = onDispose
    ) { windowPointer ->
        window = windowPointer
        when (currentExample) {
            Example.Triangle -> {
                onDispose = {}
                triangle(
                    startTime = startTime,
                    mouseX = mouseX,
                    mouseY = mouseY,
                    windowWidth = windowWidth,
                    windowHeight = windowHeight
                )
            }
            Example.Texture -> {
                onDispose = texture(
                    windowPointer = windowPointer
                )
            }
        }
    }
}
