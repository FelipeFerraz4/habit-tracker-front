package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.utils

import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

fun calculateAverageProgressPercent(
    habits: List<Habit>,
    daysToShow: Int = 30,
    referenceDate: LocalDate = LocalDate.now()
): Float {
    val start = referenceDate.minusDays(daysToShow - 1L)
    val totalPoints = habits.size * daysToShow

    if (totalPoints == 0) return 0f

    val sumRatios = habits.sumOf { habit ->
        val goal = habit.goal.coerceAtLeast(1)
        (0 until daysToShow).sumOf { offset ->
            val date = start.plusDays(offset.toLong())
            val raw = habit.progressOn(date).toDouble()
            (raw / goal).coerceAtMost(1.0)
        }
    }

    return ((sumRatios / totalPoints) * 100.0).toFloat()
}
