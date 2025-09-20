package space.algoritmos.habit_tracker.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import androidx.compose.ui.graphics.Color
import java.time.LocalDate

class HabitTest {

    private fun makeHabit(progress: Map<LocalDate, Int>): Habit {
        return Habit(
            name = "Test Habit",
            color = Color.Red,
            trackingMode = TrackingMode.BINARY,
            status = HabitStatus.ACTIVE,
            goal = 1,
            progress = progress
        )
    }

    @Test
    fun `progressOn retorna 0 quando não há progresso no dia`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(emptyMap())

        val result = habit.progressOn(today)

        assertEquals(0, result)
    }

    @Test
    fun `progressOn retorna valor correto quando há progresso`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(mapOf(today to 5))

        val result = habit.progressOn(today)

        assertEquals(5, result)
    }

    @Test
    fun `maxStreak retorna 0 quando não há progresso`() {
        val habit = makeHabit(emptyMap())

        val result = habit.maxStreak()

        assertEquals(0, result)
    }

    @Test
    fun `maxStreak calcula sequência máxima corretamente`() {
        val today = LocalDate.of(2025, 9, 4)
        val progress = mapOf(
            today to 1,
            today.minusDays(1) to 1,
            today.minusDays(2) to 1, // sequência de 3
            today.minusDays(5) to 1,
            today.minusDays(6) to 1 // sequência de 2
        )
        val habit = makeHabit(progress)

        val result = habit.maxStreak()

        assertEquals(3, result)
    }

    @Test
    fun `streakCount retorna 0 quando não há progresso hoje`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(mapOf(today.minusDays(1) to 1))

        val result = habit.streakCount(today)

        assertEquals(0, result)
    }

    @Test
    fun `streakCount conta streak a partir de hoje`() {
        val today = LocalDate.of(2025, 9, 4)
        val progress = mapOf(
            today to 1,
            today.minusDays(1) to 1,
            today.minusDays(2) to 1
        )
        val habit = makeHabit(progress)

        val result = habit.streakCount(today)

        assertEquals(3, result)
    }

    @Test
    fun `streakCount para quando há um dia sem progresso no meio`() {
        val today = LocalDate.of(2025, 9, 4)
        val progress = mapOf(
            today to 1,
            today.minusDays(2) to 1 // ontem está vazio
        )
        val habit = makeHabit(progress)

        val result = habit.streakCount(today)

        assertEquals(1, result)
    }
}
