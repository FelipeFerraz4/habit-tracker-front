package space.algoritmos.habit_tracker.ui.screens.homeScreen

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.Habit
import androidx.compose.ui.graphics.Color
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import java.time.LocalDate

class CalculateCombinedStreakTest {

    private fun makeHabit(vararg completedDates: LocalDate): Habit {
        return Habit(
            name = "Test Habit",
            color = Color.Red,
            trackingMode = TrackingMode.BINARY, // adapte conforme seu enum
            status = HabitStatus.ACTIVE,
            goal = 1,
            progress = completedDates.associateWith { 1 }
        )
    }

    @Test
    fun `retorna 0 quando lista de hábitos está vazia`() {
        val result = calculateCombinedStreak(emptyList(), LocalDate.of(2025, 9, 4))
        assertEquals(0, result)
    }

    @Test
    fun `conta streak incluindo hoje quando hábitos foram feitos hoje`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today, today.minusDays(1))
        val result = calculateCombinedStreak(listOf(habit), today)
        assertEquals(2, result)
    }

    @Test
    fun `ignora hoje e conta streak a partir de ontem quando nada foi feito hoje`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today.minusDays(1), today.minusDays(2))
        val result = calculateCombinedStreak(listOf(habit), today)
        assertEquals(2, result)
    }

    @Test
    fun `streak quebra se houver um dia sem hábitos feitos no meio`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(today, today.minusDays(2)) // ontem ficou vazio
        val result = calculateCombinedStreak(listOf(habit), today)
        assertEquals(1, result)
    }

    @Test
    fun `streak considera múltiplos hábitos`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit1 = makeHabit(today.minusDays(1))
        val habit2 = makeHabit(today.minusDays(2), today.minusDays(3))
        val result = calculateCombinedStreak(listOf(habit1, habit2), today)
        assertEquals(3, result)
    }
}
