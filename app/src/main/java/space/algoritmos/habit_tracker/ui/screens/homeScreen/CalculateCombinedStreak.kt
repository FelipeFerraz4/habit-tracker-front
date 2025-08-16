package space.algoritmos.habit_tracker.ui.screens.homeScreen

import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

fun calculateCombinedStreak(habits: List<Habit>, today: LocalDate = LocalDate.now()): Int {
    if (habits.isEmpty()) return 0

    var streak = 0
    var date = today

    while (true) {
        // Verifica se algum hábito foi feito nesse dia
        val anyDone = habits.any { it.progressOn(date) > 0f }
        if (anyDone) {
            streak++
            date = date.minusDays(1)
        } else {
            break
        }
    }

    return streak
}
