package examples

import kotlinx.cinterop.CPointer
import rgfw.RGFW_window

expect fun texture(
    windowPointer: CPointer<RGFW_window>,
    windowWidth: Int,
    windowHeight: Int
): (windowPointer: CPointer<RGFW_window>) -> Unit
