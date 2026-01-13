package examples

import kotlinx.cinterop.CPointer
import rgfw.RGFW_window

expect fun dragAndDrop(
    windowPointer: CPointer<RGFW_window>
)
