package space.algoritmos.habit_tracker.ui.screens.habitScreen.utils

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.DailyProgress
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import java.time.LocalDate
import java.util.UUID

class CalculateDayColorTest {

    private fun makeHabit(goal: Float, progress: Map<LocalDate, DailyProgress>, color: Color = Color.Red): Habit {
        return Habit(
            id = UUID.randomUUID(),
            name = "Test Habit",
            color = color,
            unit = "units",
            status = HabitStatus.ACTIVE,
            goal = goal,
            progress = progress
        )
    }

    @Test
    fun `returns light gray when progress is zero`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10f, progress = mapOf(today to DailyProgress(10f, 0f)))
        val result = calculateDayColor(habit, today)
        assertEquals(Color.LightGray.copy(alpha = 0.3f), result)
    }

    @Test
    fun `returns color with correct alpha when progress is below goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10f, progress = mapOf(today to DailyProgress(10f, 5f)), color = Color.Blue)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Blue.copy(alpha = 0.5f), result)
    }

    @Test
    fun `returns color with alpha 1 when progress equals goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10f, progress = mapOf(today to DailyProgress(10f, 10f)), color = Color.Green)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Green.copy(alpha = 1f), result)
    }

    @Test
    fun `returns color with alpha 1 when progress exceeds goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10f, progress = mapOf(today to DailyProgress(10f, 15f)), color = Color.Yellow)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Yellow.copy(alpha = 1f), result)
    }

    @Test
    fun `returns color with alpha 1 when goal is zero`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 0f, progress = mapOf(today to DailyProgress(0f, 5f)), color = Color.Magenta)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Magenta.copy(alpha = 1f), result)
    }

    @Test
    fun `treats negative goal as positive`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = -10f, progress = mapOf(today to DailyProgress(-10f, 5f)), color = Color.Red)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Red.copy(alpha = 0.5f), result)
    }

    @Test
    fun `returns color with alpha 1 when goal is negative`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = -5f, progress = mapOf(today to DailyProgress(-5f, 5f)), color = Color.Cyan)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Cyan.copy(alpha = 1f), result)
    }
}
