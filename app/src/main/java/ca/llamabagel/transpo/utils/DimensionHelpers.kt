package ca.llamabagel.transpo.utils

import android.content.res.Resources
import kotlin.math.roundToInt

fun dp(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (dp * metrics.density).roundToInt()
}