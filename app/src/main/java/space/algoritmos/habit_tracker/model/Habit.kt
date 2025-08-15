package space.algoritmos.habit_tracker.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.UUID

enum class TrackingMode {
    BINARY,       // Fiz ou não fiz
    PERCENTAGE,   // 0% a 100%
    VALUE         // Número de páginas, litros, etc.
}

data class Habit(
    val id: UUID = UUID.randomUUID(), // Gera um ID único por padrão
    val name: String,
    val color: Color,
    val trackingMode: TrackingMode,
    val goal: Float, // exemplo: 20 páginas, 3000ml, etc.
    val progress: Map<LocalDate, Float> = emptyMap()
) {
    fun progressOn(date: LocalDate): Float {
        return progress[date] ?: 0f
    }

    fun maxStreak(): Int {
        if (progress.isEmpty()) return 0

        val completedDates = progress
            .filter { it.value > 0f }
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

        while (progress[date]?.let { it > 0f } == true) {
            streak++
            date = date.minusDays(1)
        }

        return streak
    }
}
