package rhynia.nyx.api.util

import org.jetbrains.annotations.Range
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
object MathUtil {
    private val ef: DecimalFormat = DecimalFormat("#.##E0")
    private val bf: DecimalFormat = DecimalFormat("#,##0.#")
    private val LOG_2: Double = ln(2.0)

    fun safeInt(
        number: Long,
        margin:
            @Range(from = 0, to = Int.MAX_VALUE.toLong())
            Int,
    ): Int =
        if (number > (Int.MAX_VALUE - margin).toLong()) {
            Int.MAX_VALUE - margin
        } else {
            number.toInt()
        }

    fun formatE(input: Int): String = ef.format(input.toLong())

    fun formatE(input: Long): String = ef.format(input)

    fun formatE(input: BigInteger?): String = ef.format(input)

    fun clampVal(
        i: Int,
        min: Int,
        max: Int,
    ): Int = if (i < min) min else (min(i.toDouble(), max.toDouble())).toInt()

    fun clampVal(
        l: Long,
        min: Long,
        max: Long,
    ): Long = if (l < min) min else (min(l.toDouble(), max.toDouble())).toLong()

    fun clampVal(
        f: Float,
        min: Float,
        max: Float,
    ): Float = if (f < min) min else (min(f.toDouble(), max.toDouble())).toFloat()

    fun clampVal(
        d: Double,
        min: Double,
        max: Double,
    ): Double = if (d < min) min else (min(d, max))

    fun minOf(vararg values: Int): Int {
        Arrays.sort(values)
        return values[0]
    }

    fun maxOf(vararg values: Int): Int {
        Arrays.sort(values)
        return values[values.size - 1]
    }

    fun minOf(vararg values: Long): Long {
        Arrays.sort(values)
        return values[0]
    }

    fun maxOf(vararg values: Long): Long {
        Arrays.sort(values)
        return values[values.size - 1]
    }

    fun calculatePowerTier(voltage: Double): Double = 1 + max(0.0, (ln(voltage) / LOG_2) - 5) / 2
}
