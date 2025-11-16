package kgfw

import kotlinx.cinterop.ULongVarOf
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import rgfw.RGFW_readClipboard
import rgfw.RGFW_writeClipboard

fun readClipboard(): String? = memScoped {
    return RGFW_readClipboard(
        cValue<ULongVarOf<ULong>>().getPointer(this)
    )?.toKString()
}

fun writeClipboard(
    text: String
) = RGFW_writeClipboard(
    text = text,
    textLen = text.length.toUInt()
)
