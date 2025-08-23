package space.algoritmos.habit_tracker.domain.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.UUID

data class Habit(
    val id: UUID = UUID.randomUUID(), // Gera um ID único por padrão
    val name: String,
    val color: Color,
    val trackingMode: TrackingMode,
    val status: HabitStatus,
    val goal: Int, // exemplo: 20 páginas, 3000ml, etc.
    val progress: Map<LocalDate, Int> = emptyMap()
) {
    fun progressOn(date: LocalDate): Int {
        return progress[date] ?: 0
    }

    fun maxStreak(): Int {
        if (progress.isEmpty()) return 0

        val completedDates = progress
            .filter { it.value > 0 }
            .keys
            .sorted()

        if (completedDates.isEmpty()) return 0

        var maxStreak = 1
        var currentStreak = 1

        for (i in 1 until completedDates.size) {
            val previous = completedDates[i - 1]
            val current = completedDates[i]

            if (previous.plusDays(1) == current) {
                currentStreak++
            } else {
                maxStreak = maxOf(maxStreak, currentStreak)
                currentStreak = 1
            }
        }

        return maxOf(maxStreak, currentStreak)
    }

    fun streakCount(today: LocalDate = LocalDate.now()): Int {
        if (progress.isEmpty()) return 0

        var streak = 0
        var date = today

        while (progress[date]?.let { it > 0 } == true) {
            streak++
            date = date.minusDays(1)
        }

        return streak
    }
}
