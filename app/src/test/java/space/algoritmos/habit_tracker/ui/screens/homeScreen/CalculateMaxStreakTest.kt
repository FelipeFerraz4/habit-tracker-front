package space.algoritmos.habit_tracker.ui.screens.homeScreen

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.Habit
import androidx.compose.ui.graphics.Color
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateMaxStreak
import java.time.LocalDate

class CalculateMaxStreakTest {

    private fun makeHabit(vararg completedDates: LocalDate): Habit {
        return Habit(
            name = "Test Habit",
            color = Color.Red,
            trackingMode = TrackingMode.BINARY,
            status = HabitStatus.ACTIVE,
            goal = 1,
            progress = completedDates.associateWith { 1 }
        )
    }

    @Test
    fun `returns 0 when habit list is empty`() {
        val result = calculateMaxStreak(emptyList())
        assertEquals(0, result)
    }

    @Test
    fun `returns 0 when no habit has progress`() {
        val habit = Habit(
            name = "Empty Habit",
            color = Color.Blue,
            trackingMode = TrackingMode.BINARY,
            status = HabitStatus.ACTIVE,
            goal = 1,
            progress = emptyMap()
        )
        val result = calculateMaxStreak(listOf(habit))
        assertEquals(0, result)
    }

    @Test
    fun `counts continuous streak correctly`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today, today.minusDays(1), today.minusDays(2))
        val result = calculateMaxStreak(listOf(habit))
        assertEquals(3, result)
    }

    @Test
    fun `streak breaks when there is a missing day in the middle`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today, today.minusDays(2))
        val result = calculateMaxStreak(listOf(habit))
        assertEquals(1, result)
    }

    @Test
    fun `streak considers multiple habits together`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit1 = makeHabit(today.minusDays(1))
        val habit2 = makeHabit(today.minusDays(2), today.minusDays(3))
        val result = calculateMaxStreak(listOf(habit1, habit2))
        assertEquals(3, result)
    }

    @Test
    fun `returns the longest streak when there are multiple sequences`() {
        val today = LocalDate.of(2025, 9, 10)
        val habit = makeHabit(
            today, today.minusDays(1), today.minusDays(2),
            today.minusDays(5), today.minusDays(6), today.minusDays(7), today.minusDays(8) // sequence of 4
        )
        val result = calculateMaxStreak(listOf(habit))
        assertEquals(4, result)
    }
}
