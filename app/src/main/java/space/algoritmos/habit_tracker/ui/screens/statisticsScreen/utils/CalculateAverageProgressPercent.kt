package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.utils

import space.algoritmos.habit_tracker.domain.model.Habit

fun calculateAverageProgressPercent(habits: List<Habit>): Float {
    val allDates = habits.flatMap { it.progress.keys }.toSet()

    if (allDates.isEmpty()) return 0f

    val sumRatios = habits.sumOf { habit ->
        allDates.sumOf { date ->
            val daily = habit.progressOn(date)

            val goal = daily.goal.coerceAtLeast(1f)
            val raw = daily.done

            ((raw / goal).coerceAtMost(1f)).toDouble()
        }
    }

    val totalPoints = habits.size * allDates.size

    return ((sumRatios / totalPoints) * 100.0).toFloat()
}