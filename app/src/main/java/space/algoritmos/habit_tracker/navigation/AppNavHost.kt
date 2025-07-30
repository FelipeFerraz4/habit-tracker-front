package space.algoritmos.habit_tracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import space.algoritmos.habit_tracker.model.Habit
import space.algoritmos.habit_tracker.ui.screens.homeScreen.HomeScreen
import space.algoritmos.habit_tracker.ui.screens.SplashScreen

import java.time.LocalDate

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {

            val today = LocalDate.now()
            val days = (0..90).map { today.minusDays(it.toLong()) }.reversed()

            val fakeHabits = listOf(
                Habit(
                    id = 1,
                    name = "Beber Água",
                    color = Color(0xFF4CAF50),
                    progress = days.associateWith { date ->
                        // Progresso 1f nos dias pares (index par), 0f nos outros
                        if (days.indexOf(date) % 5 == 0) 0.75f else 0f
                    }
                ),
                Habit(
                    id = 2,
                    name = "Fazer Exercício",
                    color = Color(0xFFFF5722),
                    progress = days.associateWith { date ->
                        // Progresso 1f nos dias múltiplos de 3, 0f nos outros
                        if (days.indexOf(date) % 3 == 0) 1f else 0f
                    }
                ),
                Habit(
                    id = 3,
                    name = "Ler um Livro",
                    color = Color(0xFF2196F3),
                    progress = days.associateWith { date ->
                        // Progresso 1f nos dias que não são múltiplos de 4, 0f nos outros
                        if (days.indexOf(date) % 4 != 0) 0.8f else 0f
                    }
                )
            )

            HomeScreen(
                habits = fakeHabits,
                onHabitClick = { /* abrir detalhes */ },
                streakCount = 5,

                // Ainda não logado
                isLoggedIn = false,

                // Tema claro
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,

                // Ações ainda não implementadas
                onLoginClick = { /* navegar para login */ },
                onLogoutClick = { /* sair */ },
                onStatsClick = { /* navegar para estatísticas */ },

                onSyncClick = { /* sincronizar hábitos */ },
                onAddHabitClick = { /* adicionar hábito */ }
            )

        }
    }
}
