package space.algoritmos.habit_tracker.ui.screens.habitScreen.utils

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import java.time.LocalDate
import java.util.UUID

class CalculateDayColorTest {

    private fun makeHabit(goal: Int, progress: Map<LocalDate, Int>, color: Color = Color.Red): Habit {
        return Habit(
            id = UUID.randomUUID(),
            name = "Test Habit",
            color = color,
            trackingMode = TrackingMode.BINARY,
            status = HabitStatus.ACTIVE,
            goal = goal,
            progress = progress
        )
    }

    @Test
    fun `returns light gray when progress is zero`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 0))
        val result = calculateDayColor(habit, today)
        assertEquals(Color.LightGray.copy(alpha = 0.3f), result)
    }

    @Test
    fun `returns color with correct alpha when progress is below goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 5), color = Color.Blue)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Blue.copy(alpha = 0.5f), result)
    }

    @Test
    fun `returns color with alpha 1 when progress equals goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 10), color = Color.Green)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Green.copy(alpha = 1f), result)
    }

    @Test
    fun `returns color with alpha 1 when progress exceeds goal`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 10, progress = mapOf(today to 15), color = Color.Yellow)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Yellow.copy(alpha = 1f), result)
    }

    @Test
    fun `returns color with alpha 1 when goal is zero`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = 0, progress = mapOf(today to 5), color = Color.Magenta)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Magenta.copy(alpha = 1f), result)
    }

    @Test
    fun `treats negative goal as positive`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = -10, progress = mapOf(today to 5), color = Color.Red)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Red.copy(alpha = 0.5f), result)
    }

    @Test
    fun `returns color with alpha 1 when goal is negative`() {
        val today = LocalDate.now()
        val habit = makeHabit(goal = -5, progress = mapOf(today to 5), color = Color.Cyan)
        val result = calculateDayColor(habit, today)
        assertEquals(Color.Cyan.copy(alpha = 1f), result)
    }
}
