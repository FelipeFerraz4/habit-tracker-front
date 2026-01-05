package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.utils

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.DailyProgress
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import java.time.LocalDate

class CalculateAverageProgressPercentTest {

    private fun makeHabit(
        goal: Float = 100f,
        progress: Map<LocalDate, DailyProgress> = emptyMap()
    ): Habit {
        return Habit(
            name = "Test Habit",
            color = Color.Red,
            unit = "units",
            status = HabitStatus.ACTIVE,
            goal = goal,
            progress = progress
        )
    }

    @Test
    fun `returns 0 when there are no habits`() {
        val result = calculateAverageProgressPercent(emptyList(), daysToShow = 7)
        assertEquals(0f, result)
    }

    @Test
    fun `returns 0 when totalPoints is 0`() {
        val result = calculateAverageProgressPercent(emptyList())
        assertEquals(0f, result)
    }

    @Test
    fun `calculates 100 percent when progress equals goal every day`() {
        val today = LocalDate.of(2025, 10, 19)
        val progress = (0 until 5).associate { offset ->
            today.minusDays(offset.toLong()) to DailyProgress(100f, 100f)
        }
        val habit = makeHabit(goal = 100f, progress = progress)

        val result = calculateAverageProgressPercent(listOf(habit), daysToShow = 5, referenceDate = today)
        assertEquals(100f, result, 0.001f)
    }

    @Test
    fun `calculates 50 percent when progress is half the goal`() {
        val today = LocalDate.of(2025, 10, 19)
        val progress = (0 until 5).associate { offset ->
            today.minusDays(offset.toLong()) to DailyProgress(100f, 50f)
        }
        val habit = makeHabit(goal = 100f, progress = progress)

        val result = calculateAverageProgressPercent(listOf(habit), daysToShow = 5, referenceDate = today)
        assertEquals(50f, result, 0.001f)
    }

    @Test
    fun `caps at 100 percent when progress exceeds goal`() {
        val today = LocalDate.of(2025, 10, 19)
        val progress = (0 until 3).associate { offset ->
            today.minusDays(offset.toLong()) to DailyProgress(100f, 200f) // 200% progresso
        }
        val habit = makeHabit(goal = 100f, progress = progress)

        val result = calculateAverageProgressPercent(listOf(habit), daysToShow = 3, referenceDate = today)
        assertEquals(100f, result, 0.001f) // deve limitar a 100%
    }

    @Test
    fun `handles goal equal to zero safely`() {
        val today = LocalDate.of(2025, 10, 19)
        val progress = mapOf(today to DailyProgress(0f, 10f))
        val habit = makeHabit(goal = 0f, progress = progress)

        val result = calculateAverageProgressPercent(listOf(habit), daysToShow = 1, referenceDate = today)
        // goal = 0 é coerced para 1, então 10/1 = 10 → coerceAtMost(1.0) = 1.0 = 100%
        assertEquals(100f, result, 0.001f)
    }

    @Test
    fun `averages progress across multiple habits`() {
        val today = LocalDate.of(2025, 10, 19)

        val habit1 = makeHabit(goal = 100f, progress = mapOf(today to DailyProgress(100f, 100f))) // 100%
        val habit2 = makeHabit(goal = 100f, progress = mapOf(today to DailyProgress(100f, 0f)))   // 0%
        val habit3 = makeHabit(goal = 100f, progress = mapOf(today to DailyProgress(100f, 50f)))  // 50%

        val result = calculateAverageProgressPercent(
            listOf(habit1, habit2, habit3),
            daysToShow = 1,
            referenceDate = today
        )
        assertEquals(50f, result, 0.001f) // média (100 + 0 + 50)/3 = 50%
    }

    @Test
    fun `considers only last N days`() {
        val today = LocalDate.of(2025, 10, 19)
        val progress = mapOf(
            today to DailyProgress(100f, 100f),
            today.minusDays(5) to DailyProgress(100f, 100f) // fora dos últimos 3 dias
        )
        val habit = makeHabit(goal = 100f, progress = progress)

        val result = calculateAverageProgressPercent(listOf(habit), daysToShow = 3, referenceDate = today)
        assertEquals(100f / 3f, result, 0.1f) // apenas 1 dia dos 3 tem progresso
    }

    @Test
    fun `returns correct result for multiple days and habits`() {
        val today = LocalDate.of(2025, 10, 19)

        val habit1 = makeHabit(
            goal = 100f,
            progress = mapOf(
                today to DailyProgress(100f, 50f),
                today.minusDays(1) to DailyProgress(100f, 100f)
            )
        )
        val habit2 = makeHabit(
            goal = 200f,
            progress = mapOf(
                today to DailyProgress(200f, 200f),
                today.minusDays(1) to DailyProgress(200f, 100f)
            )
        )

        val result = calculateAverageProgressPercent(
            listOf(habit1, habit2),
            daysToShow = 2,
            referenceDate = today
        )

        // habit1: (0.5 + 1.0) / 2 = 0.75
        // habit2: (1.0 + 0.5) / 2 = 0.75
        // média geral = 0.75 * 100 = 75%
        assertEquals(75f, result, 0.01f)
    }
}
