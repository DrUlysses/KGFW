package examples

import rgfw.*
import kotlin.time.Clock

fun triangle(
    startTime: Long,
    mouseX: Int,
    mouseY: Int,
    windowWidth: Int,
    windowHeight: Int
) {
    val currentTime = Clock.System.now().toEpochMilliseconds()
    val elapsedSeconds = (currentTime - startTime) / 1000.0f
    val rotationAngle = elapsedSeconds * 50.0f

    glMatrixMode(GL_PROJECTION.toUInt())
    glLoadIdentity()
    glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0)

    glMatrixMode(GL_MODELVIEW.toUInt())
    glLoadIdentity()

    val glX = (mouseX.toFloat() / windowWidth.toFloat()) * 2.0f - 1.0f
    val glY = -((mouseY.toFloat() / windowHeight.toFloat()) * 2.0f - 1.0f)

    glClearColor(1f, 1f, 1f, 1f)
    glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())

    glTranslatef(glX, glY, 0.0f)
    glRotatef(rotationAngle, 0.0f, 0.0f, 1.0f)

    glBegin(GL_TRIANGLES.toUInt())
    glColor3f(1.0f, 0.0f, 0.0f); glVertex2f(-0.5f, -0.5f)
    glColor3f(0.0f, 1.0f, 0.0f); glVertex2f( 0.5f, -0.5f)
    glColor3f(0.0f, 0.0f, 1.0f); glVertex2f( 0.0f,  0.5f)
    glEnd()
}
