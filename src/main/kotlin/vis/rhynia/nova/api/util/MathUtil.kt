package vis.rhynia.nova.api.util

import java.math.BigInteger
import java.text.DecimalFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import org.jetbrains.annotations.Range

object MathUtil {
  private val ef: DecimalFormat = DecimalFormat("#.##E0")
  private val bf: DecimalFormat = DecimalFormat("#,##0.#")
  private val LOG_2: Double = ln(2.0)

  fun safeInt(number: Long, margin: @Range(from = 0, to = Int.MAX_VALUE.toLong()) Int): Int {
    return if (number > (Int.MAX_VALUE - margin).toLong()) Int.MAX_VALUE - margin
    else number.toInt()
  }

  fun formatE(input: Int): String {
    return ef.format(input.toLong())
  }

  fun formatE(input: Long): String {
    return ef.format(input)
  }

  fun formatE(input: BigInteger?): String {
    return ef.format(input)
  }

  fun clampVal(i: Int, min: Int, max: Int): Int {
    return if (i < min) min else (min(i.toDouble(), max.toDouble())).toInt()
  }

  fun clampVal(l: Long, min: Long, max: Long): Long {
    return if (l < min) min else (min(l.toDouble(), max.toDouble())).toLong()
  }

  fun clampVal(f: Float, min: Float, max: Float): Float {
    return if (f < min) min else (min(f.toDouble(), max.toDouble())).toFloat()
  }

  fun clampVal(d: Double, min: Double, max: Double): Double {
    return if (d < min) min else (min(d, max))
  }

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

  fun calculatePowerTier(voltage: Double): Double {
    return 1 + max(0.0, (ln(voltage) / LOG_2) - 5) / 2
  }
}
