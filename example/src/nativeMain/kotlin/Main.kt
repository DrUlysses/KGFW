import examples.dragAndDrop
import examples.texture
import examples.triangle
import kgfw.Event
import kgfw.buttons.Keyboard
import kgfw.window
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.pointed
import rgfw.RGFW_window
import rgfw.RGFW_window_setName
import kotlin.math.roundToInt
import kotlin.time.Clock

enum class Example {
    Triangle,
    Texture,
    DragAndDrop,
}

fun main() {
    val startTime = Clock.System.now().toEpochMilliseconds()
    var mouseX = 0
    var mouseY = 0
    var windowWidth = 800
    var windowHeight = 600
    var scaleX = 1.0f
    var scaleY = 1.0f
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

                        Keyboard.F3 if currentExample != Example.DragAndDrop -> {
                            window?.let {
                                onDispose(it)
                            }
                            currentExample = Example.DragAndDrop
                            RGFW_window_setName(window, "Example ${currentExample.name}")
                        }

                        else -> {}
                    }
                }

                is Event.KeyReleased -> {
                    println("Key ${event.button} released")
                }

                is Event.DNDInit -> {
                    println("DND init at (${event.x}, ${event.y})")
                }

                is Event.DND -> {
                    println("Dropped ${event.files.size} file(s) at (${event.x}, ${event.y})")
                    event.files.forEachIndexed { index, file ->
                        println("  [$index] $file")
                    }
                }

                is Event.WindowResized-> {
                    windowWidth = event.width
                    windowHeight = event.height
                }

                is Event.ScaleUpdated -> {
                    scaleX = event.scaleX
                    scaleY = event.scaleY
                }

                is Event.WindowMaximized -> {
                    windowWidth = event.width
                    windowHeight = event.height
                }

                is Event.WindowRestored -> {
                    windowWidth = event.width
                    windowHeight = event.height
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
                    windowHeight = windowHeight,
                    framebufferWidth = (windowWidth * scaleX).roundToInt(),
                    framebufferHeight = (windowHeight * scaleY).roundToInt()
                )
            }
            Example.Texture -> {
                onDispose = texture(
                    windowPointer = windowPointer,
                    windowWidth = windowWidth,
                    windowHeight = windowHeight
                )
            }
            Example.DragAndDrop -> {
                onDispose = {}
                dragAndDrop(windowPointer)
            }
        }
    }
}
