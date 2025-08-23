package space.algoritmos.habit_tracker.data.repository

import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import java.time.LocalDate
import java.util.UUID

class HabitRepository(private val habitDao: HabitDao) {

    fun addHabit(habit: Habit) = habitDao.addHabit(habit)

    fun getAllHabits(includeDeleted: Boolean = false): List<Habit> {
        val habits = habitDao.getAllHabits()
        return if (includeDeleted) habits else habits.filter { it.status == HabitStatus.ACTIVE }
    }

    fun getHabitById(id: UUID): Habit? = habitDao.getHabitById(id)

    fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)

    fun deleteHabitHard(id: UUID) = habitDao.deleteHabit(id)

    fun deleteHabit(id: UUID) {
        val habit = getHabitById(id) ?: return
        val deletedHabit = habit.copy(status = HabitStatus.DELETED)
        updateHabit(deletedHabit)
    }

    fun updateProgress(habitId: UUID, date: LocalDate, value: Int) {
        val habit = getHabitById(habitId) ?: return
        val newProgress = habit.progress.toMutableMap()
        newProgress[date] = value
        val updatedHabit = habit.copy(progress = newProgress)
        updateHabit(updatedHabit)
    }
}
