package space.algoritmos.habit_tracker.ui.screens.habitScreen.utils

import androidx.compose.ui.graphics.Color
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

fun calculateDayColor(habit: Habit, date: LocalDate): Color {
    val progress = habit.progressOn(date)
    val goal = kotlin.math.abs(habit.goal)

    val ratio = if (goal > 0 && progress.toFloat() < goal) {
        (progress.toFloat() / goal).coerceIn(0f, 1f)
    } else {
        1f
    }

    return if (progress == 0) {
        Color.LightGray.copy(alpha = 0.3f)
    } else {
        habit.color.copy(alpha = ratio)
    }
}
