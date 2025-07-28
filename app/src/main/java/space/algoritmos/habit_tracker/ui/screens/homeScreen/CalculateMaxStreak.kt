package space.algoritmos.habit_tracker.ui.screens.homeScreen

import space.algoritmos.habit_tracker.model.Habit

fun calculateMaxStreak(habits: List<Habit>): Int {
    if (habits.isEmpty()) return 0

    // Coleta todos os dias em que houve progresso (> 0f) em pelo menos um hábito
    val allProgressDates = habits.flatMap { it.progress.entries }
        .filter { it.value > 0f }
        .map { it.key }
        .toSet()

    if (allProgressDates.isEmpty()) return 0

    // Ordena as datas e calcula a maior sequência consecutiva
    val sortedDates = allProgressDates.sorted()
    var maxStreak = 1
    var currentStreak = 1

    for (i in 1 until sortedDates.size) {
        val prev = sortedDates[i - 1]
        val curr = sortedDates[i]
        if (curr == prev.plusDays(1)) {
            currentStreak++
            maxStreak = maxOf(maxStreak, currentStreak)
        } else {
            currentStreak = 1
        }
    }

    return maxStreak
}
