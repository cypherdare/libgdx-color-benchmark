package com.cyphercove.colorbenchmark

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.NumberUtils
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("NOTHING_TO_INLINE")
inline class Rgba (val data: Int) {
    constructor(long: Long): this(long.toInt())
    constructor(r: Int, g: Int, b: Int, a: Int) : this((r shl 24) or (g shl 16) or (b shl 8) or a)

    inline val r get() = data ushr 24
    inline val g get() = (data ushr 16) and 0xff
    inline val b get() = (data ushr 8) and 0xff
    inline val a get() = data and 0xff

    operator fun times(scale: Float) =
        when {
            scale <= 0 -> Rgba(0)
            scale > 1 -> Rgba(scaleUpColorComponent(r, scale), scaleUpColorComponent(g, scale), scaleUpColorComponent(b, scale), scaleUpColorComponent(a, scale))
            else -> Rgba((r * scale).toInt(), (g * scale).toInt(), (b * scale).toInt(), (a * scale).toInt())
        }

    operator fun div(denom: Float) =
            Rgba(divideColorComponent(r, denom), divideColorComponent(g, denom), divideColorComponent(b, denom), divideColorComponent(a, denom))
    operator fun plus(other: Rgba) =
            Rgba(min(255, r + other.r), min(255, g + other.g), min(255, b + other.b), min(255, a + other.a))
    operator fun minus(other: Rgba) =
            Rgba(max(0, r - other.r), max(0, g - other.g), max(0, b - other.b), max(0, a - other.a))
    inline fun toColor() = Color().apply { Color.rgba8888ToColor(this, data) }
    inline fun toInt() = data
    /** Converts the color as a float. Alpha is compressed to 0-254 to avoid using bits in the NaN range. */
    inline fun toFloatBits() = NumberUtils.intToFloatColor((a shl 24) or ((data and 0xff00) shl 8) or ((data and 0xff0000) ushr 8) or r)

    override fun toString(): String = "RGBA(#${data.toLong().toString(16)})"
}

/** Scales and clamps a byte-sized color component. */
private inline fun scaleUpColorComponent(component: Int, scale: Float) = min(255, ((component * scale).toInt()))
/** Divides and clamps a byte-sized color component. */
private fun divideColorComponent(component: Int, denominator: Float) = clampColorComponent((component / denominator).toInt())
private inline fun clampColorComponent(component: Int): Int {
    return when {
        (component < 0) -> 0
        (component > 255) -> 255
        else -> component
    }

}