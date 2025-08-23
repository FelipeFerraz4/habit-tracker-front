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
import space.algoritmos.habit_tracker.ui.screens.SplashScreen
import space.algoritmos.habit_tracker.ui.screens.createHabitScreen.CreateHabitScreen
import space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen.HabitRegisterScreen
import space.algoritmos.habit_tracker.ui.screens.habitScreen.HabitDetailScreen
import space.algoritmos.habit_tracker.ui.screens.homeScreen.HomeScreen
import java.time.LocalDate
import java.util.UUID
import android.util.Log

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    habitRepository: HabitRepository
) {
    // Inicializa lista de hábitos a partir do repository
    val habitsState = remember { mutableStateListOf<Habit>().apply { addAll(habitRepository.getAllHabits()) } }

    NavHost(navController = navController, startDestination = "home") {
//        composable("splash") {
//            SplashScreen(navController)
//        }

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
                    onSyncClick = { /* TODO */ },
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
                        val habit = habitsState[habitIndex]

                        // Atualiza no repository
                        habitRepository.updateProgress(habit.id, today, value)

                        // Atualiza o estado local para a UI
                        habitsState[habitIndex] = habit.copy(
                            progress = habit.progress.toMutableMap().apply {
                                put(today, value)
                            }
                        )

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
                        habitRepository.addHabit(newHabit)
                        habitsState.add(newHabit)
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
    }
}
