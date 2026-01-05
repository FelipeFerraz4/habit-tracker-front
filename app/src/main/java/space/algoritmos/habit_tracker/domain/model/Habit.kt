package space.algoritmos.habit_tracker.domain.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.UUID

data class Habit(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val color: Color,
    val status: HabitStatus,
    val unit: String,
    val goal: Float,
    val progress: Map<LocalDate, DailyProgress> = emptyMap()
) {
    fun progressOn(date: LocalDate): DailyProgress {
        return progress[date] ?: DailyProgress(0f, 0f)
    }

    fun totalProgress(): Float {
        return progress.values
            .sumOf { it.done.toDouble() }
            .toFloat()
    }

    fun average(): Float {
        return progress.values
            .map { it.done.toDouble() }
            .average()
            .toFloat()
    }

    fun maxStreak(): Int {
        if (progress.isEmpty()) return 0

        val completedDates = progress
            .filter { it.value.done > 0f }
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

        while (progress[date]?.let { it.done > 0f } == true) {
            streak++
            date = date.minusDays(1)
        }

        return streak
    }
}
