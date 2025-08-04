package space.algoritmos.habit_tracker.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import space.algoritmos.habit_tracker.model.Habit
import space.algoritmos.habit_tracker.model.TrackingMode
import space.algoritmos.habit_tracker.ui.screens.SplashScreen
import space.algoritmos.habit_tracker.ui.screens.createHabitScreen.CreateHabitScreen
import space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen.HabitRegisterScreen
import space.algoritmos.habit_tracker.ui.screens.habitScreen.HabitDetailScreen
import space.algoritmos.habit_tracker.ui.screens.homeScreen.HomeScreen
import java.time.LocalDate
import kotlin.random.Random

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val today = LocalDate.now()
    val days = (0..180).map { today.minusDays(it.toLong()) }.reversed()

    val fakeHabits = remember {
        mutableStateListOf(
            Habit(
                id = 0,
                name = "Beber Água",
                color = Color(0xFF4CAF50),
                progress = days.associateWith { date ->
                    if (days.indexOf(date) % 5 == 0 || days.indexOf(date) % 9 == 0) 0.5f + Random.nextFloat() * 0.5f else 0f
                },
                trackingMode = TrackingMode.PERCENTAGE,
                goal = 3000f,
            ),
            Habit(
                id = 1,
                name = "Fazer Exercício",
                color = Color(0xFFFF5722),
                progress = days.associateWith { date ->
                    if (days.indexOf(date) % 3 == 0 || days.indexOf(date) % 7 == 0) 1f else 0f
                },
                trackingMode = TrackingMode.BINARY,
                goal = 1f
            ),
            Habit(
                id = 2,
                name = "Ler um Livro",
                color = Color(0xFF2196F3),
                progress = days.associateWith { date ->
                    if (days.indexOf(date) % 4 != 0) 0.5f + Random.nextFloat() * 0.5f else 0f
                },
                trackingMode = TrackingMode.VALUE,
                goal = 20f
            )
        )
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("home") {
            HomeScreen(
                habits = fakeHabits,
                onHabitClick = { habit ->
                    navController.navigate("habitDetail/${habit.id}")
                },
                streakCount = 5,
                isLoggedIn = false,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onLoginClick = { /* TODO */ },
                onLogoutClick = { /* TODO */ },
                onStatsClick = { /* TODO */ },
                onSyncClick = { /* TODO */ },onAddHabitClick = {
                    navController.navigate("habitCreate")
                }
            )
        }

        composable("habitDetail/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
            val habit = fakeHabits.find { it.id == habitId }

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
            val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
            val habitIndex = fakeHabits.indexOfFirst { it.id == habitId }

            if (habitIndex != -1) {
                HabitRegisterScreen(
                    habit = fakeHabits[habitIndex],
                    onSave = { value ->
                        val today = LocalDate.now()
                        val habit = fakeHabits[habitIndex]
                        fakeHabits[habitIndex] = habit.copy(
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
                    fakeHabits.add(newHabit)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                },
                habits = fakeHabits,
            )
        }
    }
}