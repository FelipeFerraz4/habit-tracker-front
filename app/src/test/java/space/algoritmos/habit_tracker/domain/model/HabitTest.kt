package space.algoritmos.habit_tracker.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.UUID

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
    fun `progressOn returns 0 when there is no progress on the day`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(emptyMap())

        val result = habit.progressOn(today)

        assertEquals(0, result)
    }

    @Test
    fun `progressOn returns correct value when there is progress`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(mapOf(today to 5))

        val result = habit.progressOn(today)

        assertEquals(5, result)
    }

    @Test
    fun `maxStreak returns 0 when there is no progress`() {
        val habit = makeHabit(emptyMap())

        val result = habit.maxStreak()

        assertEquals(0, result)
    }

    @Test
    fun `maxStreak calculates the longest sequence correctly`() {
        val today = LocalDate.of(2025, 9, 4)
        val progress = mapOf(
            today to 1,
            today.minusDays(1) to 1,
            today.minusDays(2) to 1, // sequence of 3
            today.minusDays(5) to 1,
            today.minusDays(6) to 1 // sequence of 2
        )
        val habit = makeHabit(progress)

        val result = habit.maxStreak()

        assertEquals(3, result)
    }

    @Test
    fun `streakCount returns 0 when there is no progress today`() {
        val today = LocalDate.of(2025, 9, 4)
        val habit = makeHabit(mapOf(today.minusDays(1) to 1))

        val result = habit.streakCount(today)

        assertEquals(0, result)
    }

    @Test
    fun `streakCount counts streak starting from today`() {
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
    fun `streakCount stops when there is a missing day in between`() {
        val today = LocalDate.of(2025, 9, 4)
        val progress = mapOf(
            today to 1,
            today.minusDays(2) to 1 // yesterday is missing
        )
        val habit = makeHabit(progress)

        val result = habit.streakCount(today)

        assertEquals(1, result)
    }

    @Test
    fun `negativeOrZeroProgress does not count towards streak`() {
        val today = LocalDate.now()
        val habit = makeHabit(mapOf(
            today to 0,
            today.minusDays(1) to -3,
            today.minusDays(2) to 1
        ))
        assertEquals(0, habit.streakCount(today)) // today is 0, so streak breaks
        assertEquals(1, habit.maxStreak()) // only counts day -2
    }

    @Test
    fun `futureProgress does not affect today`() {
        val today = LocalDate.now()
        val future = today.plusDays(5)
        val habit = makeHabit(mapOf(
            future to 1
        ))
        assertEquals(0, habit.progressOn(today)) // future should not affect today
        assertEquals(0, habit.streakCount(today)) // today's streak is 0
    }

    @Test
    fun `progressCountsEvenWithGoalZero`() {
        val today = LocalDate.now()
        val habit = Habit(
            id = UUID.randomUUID(),
            name = "Habit with goal 0",
            color = Color.Green,
            trackingMode = TrackingMode.BINARY,
            goal = 0, // incoherent
            progress = mapOf(today to 5),
            status = HabitStatus.ACTIVE
        )
        assertEquals(1, habit.streakCount(today))
        assertEquals(1, habit.maxStreak())
    }
}
