package space.algoritmos.habit_tracker.ui.screens.homeScreen.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.UUID

class DailyProgressTest {

    private fun makeHabit(goal: Int, progress: Map<LocalDate, Int>): Habit {
        return Habit(
            id = UUID.randomUUID(),
            name = "Test Habit",
            color = Color.Red,
            trackingMode = TrackingMode.BINARY,
            status = HabitStatus.ACTIVE,
            goal = goal,
            progress = progress
        )
    }

    @Test
    fun `returns 0f when habits list is empty`() {
        val today = LocalDate.now()
        val result = dailyProgress(today, emptyList())
        assertEquals(0f, result)
    }

    @Test
    fun `returns correct ratio when progress is below goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 5))
        val result = dailyProgress(today, listOf(habit))
        assertEquals(0.5f, result)
    }

    @Test
    fun `returns 1f when progress exceeds goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 15))
        val result = dailyProgress(today, listOf(habit))
        assertEquals(1f, result)
    }

    @Test
    fun `returns 0f when goal is zero or negative`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 0, progress = mapOf(today to 10))
        val result = dailyProgress(today, listOf(habit))
        assertEquals(0f, result)
    }

    @Test
    fun `averages multiple habits correctly`() {
        val today = LocalDate.now()
        val habit1 = makeHabit(goal = 10, progress = mapOf(today to 5))   // 0.5
        val habit2 = makeHabit(goal = 20, progress = mapOf(today to 10))  // 0.5
        val habit3 = makeHabit(goal = 10, progress = mapOf(today to 15))  // 1.0 (capped)
        val result = dailyProgress(today, listOf(habit1, habit2, habit3))
        assertEquals(0.666f, result, 0.001f) // média = (0.5 + 0.5 + 1.0) / 3
    }

    @Test
    fun `result is clamped between 0f and 1f`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 100))
        val result = dailyProgress(today, listOf(habit))
        assertEquals(1f, result) // mesmo sendo 10x maior, fica em 1f
    }
}
