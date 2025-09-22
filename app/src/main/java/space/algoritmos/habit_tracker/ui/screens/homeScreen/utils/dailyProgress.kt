package space.algoritmos.habit_tracker.ui.screens.homeScreen.utils

import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

fun dailyProgress(date: LocalDate, habits: List<Habit>): Float {
    if (habits.isEmpty()) return 0f

    return habits
        .map { habit ->
            val progress = habit.progressOn(date).toFloat()
            when {
                habit.goal <= 0 -> 0f
                progress > habit.goal -> 1f
                else -> progress / habit.goal
            }
        }
        .average()
        .toFloat()
        .coerceIn(0f, 1f)
}