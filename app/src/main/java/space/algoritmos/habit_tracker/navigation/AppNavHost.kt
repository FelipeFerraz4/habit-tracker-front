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

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getLastNDaysDates(n: Int): List<String> {
    val dates = mutableListOf<String>()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    for (i in 0 until n) {
        dates.add(formatter.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, -1)
    }

    return dates.reversed() // opcional, para deixar do mais antigo para o mais recente
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {
            // Gerar os últimos 7 dias dinâmicos
            val last7Days = getLastNDaysDates(7)

            val fakeHabits = listOf(
                Habit(
                    id = 1,
                    name = "Beber Água",
                    color = Color(0xFF4CAF50),
                    completedDates = last7Days.filterIndexed { index, _ -> index % 2 == 0 } // exemplo: concluiu em dias pares
                ),
                Habit(
                    id = 2,
                    name = "Fazer Exercício",
                    color = Color(0xFFFF5722),
                    completedDates = last7Days.filterIndexed { index, _ -> index % 3 == 0 } // exemplo: concluiu em dias múltiplos de 3
                ),
                Habit(
                    id = 3,
                    name = "Ler um Livro",
                    color = Color(0xFF2196F3),
                    completedDates = last7Days.filterIndexed { index, _ -> index % 4 != 0 } // exemplo: concluiu em quase todos os dias
                ),
            )

            HomeScreen(
                onMenuClick = { /* abrir drawer ou menu */ },
                onSyncClick = { /* sincronizar com backend */ },
                onAddHabitClick = { /* navegar para criar hábito */ },
                habits = fakeHabits,
                onHabitClick = { habit ->
                    // exemplo: navegar para os detalhes do hábito
                    // navController.navigate("habitDetail/${habit.id}")
                },
                streakCount = 7 // exemplo de dias seguidos
            )
        }
    }
}
