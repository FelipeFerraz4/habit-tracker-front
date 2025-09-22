package space.algoritmos.habit_tracker.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.createHabitScreen.CreateHabitScreen
import space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen.HabitRegisterScreen
import space.algoritmos.habit_tracker.ui.screens.habitScreen.HabitDetailScreen
import space.algoritmos.habit_tracker.ui.screens.homeScreen.HomeScreen
import java.time.LocalDate
import java.util.UUID
import android.util.Log
import space.algoritmos.habit_tracker.navigation.utils.addHabit
import space.algoritmos.habit_tracker.navigation.utils.deleteHabit
import space.algoritmos.habit_tracker.navigation.utils.updateHabit
import space.algoritmos.habit_tracker.navigation.utils.updateHabitProgress
import space.algoritmos.habit_tracker.ui.screens.editHabitScreen.EditHabitScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    habitRepository: HabitRepository
) {
    // Initialize habits list from repository
    val habitsState = remember { mutableStateListOf<Habit>().apply { addAll(habitRepository.getAllHabits()) } }

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                habits = habitsState,
                onHabitClick = { habit ->
                    navController.navigate("habitDetail/${habit.id}")
                },
                isLoggedIn = false,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onLoginClick = { /* TODO */ },
                onLogoutClick = { /* TODO */ },
                onStatsClick = { /* TODO */ },
                onSyncClick = { /* TODO */ },
                onAddHabitClick = {
                    navController.navigate("habitCreate")
                }
            )
        }

        composable("habitDetail/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.let { UUID.fromString(it) }
            val habit = habitsState.find { it.id == habitId }

            if (habit != null) {
                HabitDetailScreen(
                    habit = habit,
                    onRegisterClick = {
                        navController.navigate("habitRegister/${habit.id}")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("habitEdit/${habit.id}")
                    },
                )
            } else {
                Text("Hábito não encontrado.")
            }
        }

        composable("habitRegister/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.let { UUID.fromString(it) }
            val habitIndex = habitsState.indexOfFirst { it.id == habitId }

            if (habitIndex != -1) {
                HabitRegisterScreen(
                    habit = habitsState[habitIndex],
                    onSave = { value ->
                        val today = LocalDate.now()
                        val updatedHabit = updateHabitProgress(habitsState[habitIndex], today, value, habitRepository)
                        habitsState[habitIndex] = updatedHabit
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Hábito não encontrado.")
            }
        }

        composable("habitCreate") {
            CreateHabitScreen(
                onSave = { newHabit ->
                    try {
                        Log.d("HabitDebug", "Tentando salvar hábito: $newHabit")
                        val updatedList = addHabit(habitsState, newHabit, habitRepository)
                        habitsState.clear()
                        habitsState.addAll(updatedList)
                        Log.d("HabitDebug", "Hábito salvo com sucesso!")
                        navController.popBackStack()
                    } catch (e: Exception) {
                        Log.e("HabitDebug", "Erro ao salvar hábito", e)
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable("habitEdit/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.let { UUID.fromString(it) }
            val habitIndex = habitsState.indexOfFirst { it.id == habitId }

            if (habitIndex != -1) {
                EditHabitScreen(
                    habit = habitsState[habitIndex],
                    onSave = { updatedHabit ->
                        val updatedList = updateHabit(habitsState, updatedHabit, habitRepository)
                        habitsState.clear()
                        habitsState.addAll(updatedList)
                        navController.popBackStack()
                    },
                    onDelete = {
                        val updatedList = deleteHabit(habitsState, habitsState[habitIndex].id, habitRepository)
                        habitsState.clear()
                        habitsState.addAll(updatedList)
                        navController.popBackStack("home", inclusive = false)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Hábito não encontrado.")
            }
        }
    }
}
