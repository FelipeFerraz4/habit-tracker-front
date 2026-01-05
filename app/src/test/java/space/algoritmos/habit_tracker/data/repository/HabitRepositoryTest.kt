package space.algoritmos.habit_tracker.data.repository

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.domain.model.*
import java.time.LocalDate
import java.util.*

class HabitRepositoryTest {

    private lateinit var habitDao: HabitDao
    private lateinit var repository: HabitRepository
    private lateinit var habit: Habit

    @BeforeEach
    fun setUp() {
        habitDao = mock()
        repository = HabitRepository(habitDao)

        habit = Habit(
            id = UUID.randomUUID(),
            name = "Drink Water",
            color = Color.Blue,
            goal = 2000f,
            unit = "ml",
            progress = emptyMap(),
            status = HabitStatus.ACTIVE
        )
    }

    @Test
    fun `getAllHabits filters out deleted when includeDeleted is false`() {
        whenever(habitDao.getAllHabits()).thenReturn(
            listOf(
                habit,
                habit.copy(status = HabitStatus.DELETED)
            )
        )

        val result = repository.getAllHabits(includeDeleted = false)

        assertEquals(1, result.size)
        assertTrue(result.all { it.status == HabitStatus.ACTIVE })
    }

    @Test
    fun `deleteHabit performs soft delete`() {
        whenever(habitDao.getHabitById(habit.id)).thenReturn(habit)

        repository.deleteHabit(habit.id)

        verify(habitDao).updateHabit(
            check {
                assertEquals(HabitStatus.DELETED, it.status)
            }
        )
    }

    @Test
    fun `updateProgress adds new entry to progress`() {
        whenever(habitDao.getHabitById(habit.id)).thenReturn(habit)

        val today = LocalDate.now()
        repository.updateProgress(habit.id, today, 500f, habit.goal)

        verify(habitDao).updateHabit(
            check {
                assertEquals(DailyProgress(habit.goal, 500f), it.progress[today])
            }
        )
    }

    @Test
    fun `deleteHabitHard delegates directly to dao`() {
        repository.deleteHabitHard(habit.id)
        verify(habitDao).deleteHabit(habit.id)
    }
}
