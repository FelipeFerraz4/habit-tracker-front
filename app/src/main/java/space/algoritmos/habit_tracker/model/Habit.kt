package space.algoritmos.habit_tracker.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class Habit(
    val id: Int,
    val name: String,
    val color: Color,
    val progress: Map<LocalDate, Float> = emptyMap()
) {
    fun wasCompletedOn(date: LocalDate): Boolean {
        return (progress[date] ?: 0f) > 0f
    }

    fun progressOn(date: LocalDate): Float {
        return progress[date] ?: 0f
    }
}
