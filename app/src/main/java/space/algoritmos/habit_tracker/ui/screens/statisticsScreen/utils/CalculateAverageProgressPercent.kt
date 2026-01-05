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
        (0 until daysToShow).sumOf { offset ->
            val date = start.plusDays(offset.toLong())
            val daily = habit.progressOn(date)

            val goal = daily.goal.coerceAtLeast(1f)
            val raw = daily.done

            ((raw / goal).coerceAtMost(1f)).toDouble()
        }
    }

    return ((sumRatios / totalPoints) * 100.0).toFloat()
}
