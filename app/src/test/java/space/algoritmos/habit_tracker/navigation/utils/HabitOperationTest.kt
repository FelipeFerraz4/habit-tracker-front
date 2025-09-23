package space.algoritmos.habit_tracker.navigation.utils

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import java.time.LocalDate
import java.util.*

class HabitOperationTest {

    private lateinit var habitRepository: HabitRepository
    private lateinit var sampleHabit: Habit

    @BeforeEach
    fun setup() {
        habitRepository = mock()
        sampleHabit = Habit(
            id = UUID.randomUUID(),
            name = "Test Habit",
            color = Color.Companion.Red,
            trackingMode = TrackingMode.BINARY,
            status = HabitStatus.ACTIVE,
            goal = 1,
            progress = mapOf(
                LocalDate.now().minusDays(3) to 1,
                LocalDate.now().minusDays(2) to 1,
                LocalDate.now().minusDays(1) to 1,
                LocalDate.now() to 1
            )
        )
    }

    @Test
    fun `updateHabitProgress should update progress and call repository`() {
        val date = LocalDate.now()
        val updated = updateHabitProgress(sampleHabit, date, 5, habitRepository)

        // repository called
        verify(habitRepository).updateProgress(sampleHabit.id, date, 5)

        // progress updated
        assertEquals(5, updated.progress[date])
    }

    @Test
    fun `addHabit should add habit to list and call repository`() {
        val habits = mutableListOf<Habit>()
        val result = addHabit(habits, sampleHabit, habitRepository)

        verify(habitRepository).addHabit(sampleHabit)
        assertTrue(result.contains(sampleHabit))
        assertEquals(1, result.size)
    }

    @Test
    fun `updateHabit should replace habit in list and call repository`() {
        val updatedHabit = sampleHabit.copy(name = "Read More")
        val habits = mutableListOf(sampleHabit)

        val result = updateHabit(habits, updatedHabit, habitRepository)

        verify(habitRepository).updateHabit(updatedHabit)
        assertEquals("Read More", result[0].name)
    }

    @Test
    fun `deleteHabit should remove habit from list and call repository`() {
        val habits = mutableListOf(sampleHabit)

        val result = deleteHabit(habits, sampleHabit.id, habitRepository)

        verify(habitRepository).deleteHabit(sampleHabit.id)
        assertFalse(result.contains(sampleHabit))
        assertTrue(result.isEmpty())
    }
}
