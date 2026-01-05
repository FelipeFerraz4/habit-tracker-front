package space.algoritmos.habit_tracker.ui.screens.homeScreen.utils

import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

fun dailyProgress(date: LocalDate, habits: List<Habit>): Float {
    if (habits.isEmpty()) return 0f

    return habits
        .map { habit ->
            val progress = habit.progressOn(date).done
            when {
                habit.progressOn(date).goal <= 0f -> 0f
                progress > habit.progressOn(date).goal-> 1f
                else -> progress / habit.progressOn(date).goal
            }
        }
        .average()
        .toFloat()
        .coerceIn(0f, 1f)
}