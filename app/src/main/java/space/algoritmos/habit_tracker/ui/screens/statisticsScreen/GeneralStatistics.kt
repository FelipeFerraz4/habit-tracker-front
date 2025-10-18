package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateCombinedStreak
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateMaxStreak
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.DailyAggregateLineChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.DailyHabitCountBarChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.GeneralHabitsProgressLineChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.WeeklyBarChart
import java.time.LocalDate

@Composable
fun GeneralStatistics(
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    val totalHabits = habits.size
    val totalDays = habits.sumOf { it.progress.size }
    val combinedStreak = calculateCombinedStreak(habits, LocalDate.now())
    val maxStreak = calculateMaxStreak(habits)
    val avgProgress = if (totalDays > 0) {
        habits.sumOf { it.progress.values.sum() }.toFloat() / totalDays
    } else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estatísticas Gerais",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        StatisticItem("Hábitos Ativos", totalHabits.toString())
        StatisticItem("Dias Registrados", totalDays.toString())
        StatisticItem("Sequência Atual Combinada", "$combinedStreak dias")
        StatisticItem("Maior Sequência Combinada", "$maxStreak dias")
        StatisticItem("Média de Progresso por Dia", "%.1f".format(avgProgress))

        Spacer(Modifier.height(16.dp))

        Text("Hábitos concluídos", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        DailyHabitCountBarChart(habits)

        Spacer(Modifier.height(16.dp))

        Text("Evolução dos hábitos", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        // Agregado semanal geral
        GeneralHabitsProgressLineChart(habits = habits)

        Spacer(Modifier.height(12.dp))

        // Linha diária agregada geral (somando todos os hábitos)
        DailyAggregateLineChart(habits)

        Spacer(Modifier.height(16.dp))
    }
}
