import kgfw.Event
import kgfw.window
import rgfw.*
import kotlin.time.Clock

fun main() {
    val startTime = Clock.System.now().toEpochMilliseconds()
    var mouseX = 0
    var mouseY = 0
    val windowWidth = 800
    val windowHeight = 600

    window(
        name = "Example",
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
                }

                is Event.KeyReleased -> {
                    println("Key ${event.button} released")
                }

                else -> {}
            }
        }
    ) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val elapsedSeconds = (currentTime - startTime) / 1000.0f
        val rotationAngle = elapsedSeconds * 50.0f // 50 degrees per second

        // Convert screen coordinates to OpenGL coordinates (-1 to 1)
        val glX = (mouseX.toFloat() / windowWidth.toFloat()) * 2.0f - 1.0f
        val glY = -((mouseY.toFloat() / windowHeight.toFloat()) * 2.0f - 1.0f) // Flip Y axis

        glClearColor(red = 1f, green = 1f, blue = 1f, alpha = 1f)
        glClear(mask = (GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())

        glLoadIdentity()
        glTranslatef(x = glX, y = glY, z = 0.0f)
        glRotatef(angle = rotationAngle, x = 0.0f, y = 0.0f, z = 1.0f)

        glBegin(mode = GL_TRIANGLES.toUInt())

        glColor3f(red = 1.0f, green = 0.0f, blue = 0.0f)
        glVertex2f(x = -0.5f, y = -0.5f)

        glColor3f(red = 0.0f, green = 1.0f, blue = 0.0f)
        glVertex2f(x = 0.5f, y = -0.5f)

        glColor3f(red = 0.0f, green = 0.0f, blue = 1.0f)
        glVertex2f(x = 0.0f, y = 0.5f)

        glEnd()
    }
}
