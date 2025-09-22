package space.algoritmos.habit_tracker.navigation.utils

import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import java.time.LocalDate
import java.util.*

/**
 * Updates the progress of a habit on a specific date.
 * Returns a new Habit instance with the updated progress.
 */
fun updateHabitProgress(habit: Habit, date: LocalDate, value: Int, habitRepository: HabitRepository): Habit {
    habitRepository.updateProgress(habit.id, date, value)
    return habit.copy(
        progress = habit.progress.toMutableMap().apply {
            put(date, value)
        }
    )
}

/**
 * Adds a new habit to the repository and returns the updated list of habits.
 */
fun addHabit(habits: MutableList<Habit>, newHabit: Habit, habitRepository: HabitRepository): List<Habit> {
    habitRepository.addHabit(newHabit)
    return habits.toMutableList().apply {
        add(newHabit)
    }
}

/**
 * Updates an existing habit in the repository and returns the updated list.
 */
fun updateHabit(habits: MutableList<Habit>, updatedHabit: Habit, habitRepository: HabitRepository): List<Habit> {
    habitRepository.updateHabit(updatedHabit)
    return habits.toMutableList().apply {
        val index = indexOfFirst { it.id == updatedHabit.id }
        if (index != -1) {
            set(index, updatedHabit)
        }
    }
}

/**
 * Deletes a habit from the repository and returns the updated list of habits.
 */
fun deleteHabit(habits: MutableList<Habit>, habitId: UUID, habitRepository: HabitRepository): List<Habit> {
    habitRepository.deleteHabit(habitId)
    return habits.toMutableList().apply {
        removeAll { it.id == habitId }
    }
}
