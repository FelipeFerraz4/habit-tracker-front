package space.algoritmos.habit_tracker.data.local.converters

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

object ColorConverter {
    fun fromString(value: String?): Color? {
        return value?.let { Color(it.toColorInt()) }
    }

    fun toString(color: Color?): String? {
        return color?.let {
            String.format("#%08X", it.value.toLong() and 0xFFFFFFFF)
        }
    }
}
