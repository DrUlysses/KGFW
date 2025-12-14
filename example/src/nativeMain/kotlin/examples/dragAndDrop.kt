package examples

import kotlinx.cinterop.CPointer
import rgfw.RGFW_window
import rgfw.glClear
import rgfw.GL_COLOR_BUFFER_BIT
import rgfw.glClearColor

/**
 * Drag and drop example.
 *
 * Use F3 in the example launcher to switch to this example, then drag files onto the window.
 * Dropped file paths and their count are printed by Main.kt's event handler when it receives Event.DND.
 */
fun dragAndDrop(
    windowPointer: CPointer<RGFW_window>
) {
    // Keep a visible background; events are handled in Main.kt.
    glClearColor(0.12f, 0.12f, 0.12f, 1f)
    glClear(GL_COLOR_BUFFER_BIT.toUInt())
}
