package space.algoritmos.habit_tracker.ui.screens.homeScreen.utils

import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

fun calculateCombinedStreak(habits: List<Habit>, today: LocalDate = LocalDate.now()): Int {
    if (habits.isEmpty()) return 0

    var streak = 0
    var date = today

    // Primeiro, se não há progresso hoje, começa de ontem
    if (habits.none { it.progressOn(today).done > 0f }) {
        date = today.minusDays(1)
    }

    // Agora conta streak para trás
    while (true) {
        val anyDone = habits.any { it.progressOn(date).done > 0f }
        if (anyDone) {
            streak++
            date = date.minusDays(1)
        } else {
            break
        }
    }

    return streak
}
