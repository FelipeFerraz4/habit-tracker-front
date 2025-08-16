package space.algoritmos.habit_tracker.data.repository

import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate
import java.util.UUID

class HabitRepository(private val habitDao: HabitDao) {

    fun addHabit(habit: Habit) = habitDao.addHabit(habit)

    fun getAllHabits(): List<Habit> = habitDao.getAllHabits()

    fun getHabitById(id: UUID): Habit? = habitDao.getHabitById(id)

    fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)

    fun deleteHabit(id: UUID) = habitDao.deleteHabit(id)

    /**
     * Atualiza o progresso diário de um hábito.
     * Se o hábito não existir, não faz nada.
     */
    fun updateProgress(habitId: UUID, date: LocalDate, value: Float) {
        val habit = getHabitById(habitId) ?: return
        val newProgress = habit.progress.toMutableMap()
        newProgress[date] = value
        val updatedHabit = habit.copy(progress = newProgress)
        updateHabit(updatedHabit)
    }
}
