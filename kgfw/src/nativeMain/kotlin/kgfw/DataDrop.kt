package kgfw

import kotlinx.cinterop.get
import kotlinx.cinterop.toKString
import rgfw.RGFW_dataDropEvent

fun RGFW_dataDropEvent.toFileList(): List<String> {
    if (files == null || count <= 0.toULong()) {
        return emptyList()
    }

    val safeCount = count.toInt().coerceAtMost(maximumValue = Int.MAX_VALUE)
    val result = ArrayList<String>(initialCapacity = safeCount)
    for (i in 0 until safeCount) {
        val cstr = files?.get(i) ?: continue
        result.add(cstr.toKString())
    }
    return result
}
