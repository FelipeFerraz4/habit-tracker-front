package space.algoritmos.habit_tracker.model

import androidx.compose.ui.graphics.Color

data class Habit(
    val id: Int,
    val name: String,
    val color: Color,
    val completedDates: List<String> = emptyList()  // datas no formato "yyyy-MM-dd"
) {
    fun wasCompletedOn(date: String): Boolean {
        return completedDates.contains(date)
    }
}
