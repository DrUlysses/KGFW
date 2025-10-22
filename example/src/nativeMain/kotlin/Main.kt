import kgfw.Event
import kgfw.window
import rgfw.GL_COLOR_BUFFER_BIT
import rgfw.GL_DEPTH_BUFFER_BIT
import rgfw.GL_TRIANGLES
import rgfw.glBegin
import rgfw.glClear
import rgfw.glClearColor
import rgfw.glColor3f
import rgfw.glEnd
import rgfw.glVertex2f
import kotlin.io.println

fun main() {
    window(
        name = "Example",
        width = 800,
        height = 600,
        onEvent = { event ->
            when (event) {
                is Event.Quit -> {
                    println("Quit")
                }

                is Event.MouseButtonPressed -> {
                    println("${event.x} , ${event.y}")
                }
            }
        }
    ) {
        glClearColor(red = 1f, green = 1f, blue = 1f, alpha = 1f)
        glClear(mask = (GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())
        glBegin(mode = GL_TRIANGLES.toUInt())
        glColor3f(red = 1.0f, green = 0.0f, blue = 0.0f)
        glVertex2f(x = -0.6f, y = -0.75f)
        glColor3f(red = 0.0f, green = 1.0f, blue = 0.0f)
        glVertex2f(x = 0.6f, y = -0.75f)
        glColor3f(red = 0.0f, green = 0.0f, blue = 1.0f)
        glVertex2f(x = 0.0f, y = 0.75f)
        glEnd()
    }
}
