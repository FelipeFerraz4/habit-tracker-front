package space.algoritmos.habit_tracker.ui.screens.homeScreen.utils

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.DailyProgress
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import java.time.LocalDate

class CalculateCombinedStreakTest {

    private fun makeHabit(vararg completedDates: LocalDate): Habit {
        return Habit(
            name = "Test Habit",
            color = Color.Companion.Red,
            unit = "units",
            status = HabitStatus.ACTIVE,
            goal = 1f,
            progress = completedDates.associateWith { DailyProgress(1f, 1f) }
        )
    }

    @Test
    fun `returns 0 when habit list is empty`() {
        val result = calculateCombinedStreak(emptyList(), LocalDate.of(2025, 9, 4))
        Assertions.assertEquals(0, result)
    }

    @Test
    fun `counts streak including today when habits are done today`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today, today.minusDays(1))
        val result = calculateCombinedStreak(listOf(habit), today)
        Assertions.assertEquals(2, result)
    }

    @Test
    fun `ignores today and counts streak from yesterday when nothing is done today`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today.minusDays(1), today.minusDays(2))
        val result = calculateCombinedStreak(listOf(habit), today)
        Assertions.assertEquals(2, result)
    }

    @Test
    fun `streak breaks if there is a missing day in the middle`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today, today.minusDays(2)) // yesterday is missing
        val result = calculateCombinedStreak(listOf(habit), today)
        Assertions.assertEquals(1, result)
    }

    @Test
    fun `streak considers multiple habits together`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit1 = makeHabit(today.minusDays(1))
        val habit2 = makeHabit(today.minusDays(2), today.minusDays(3))
        val result = calculateCombinedStreak(listOf(habit1, habit2), today)
        Assertions.assertEquals(3, result)
    }
}