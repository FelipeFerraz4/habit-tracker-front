package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateCombinedStreak
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateMaxStreak
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.DailyHabitCountBarChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.GeneralHabitsProgressLineChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.WeeklyProgressPieChart
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

@Composable
fun GeneralStatistics(
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    val totalHabits = habits.size
    val totalDays = habits.sumOf { it.progress.size }
    val combinedStreak = calculateCombinedStreak(habits, LocalDate.now())
    val maxStreak = calculateMaxStreak(habits)
    val daysToShow = 30
    val today = LocalDate.now()
    val start = today.minusDays(daysToShow - 1L)
    val totalPoints = habits.size * daysToShow

    val sumRatios = habits.sumOf { habit ->
        val goal = habit.goal.coerceAtLeast(1)
        (0 until daysToShow).sumOf { offset ->
            val date = start.plusDays(offset.toLong())
            val raw = habit.progressOn(date).toDouble()
            (raw / goal).coerceAtMost(1.0) // limite opcional a 100%
        }
    }
    val avgProgressPercent30d = if (totalPoints > 0) {
        ((sumRatios / totalPoints) * 100.0).toFloat()
    } else 0f

    // Semana atual começando na segunda-feira
    val startOfWeek = remember {
        LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

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

        StatisticItem("Número de Hábitos", totalHabits.toString())
        StatisticItem("Dias Registrados", totalDays.toString())
        StatisticItem("Ofensiva Atual", "$combinedStreak dias")
        StatisticItem("Maior Ofensiva Registrado", "$maxStreak dias")
        StatisticItem("Média de Progresso por Dia", "%.1f".format(avgProgressPercent30d))

        Spacer(Modifier.height(16.dp))

        Text("Hábitos Concluídos", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        DailyHabitCountBarChart(habits)

        Spacer(Modifier.height(16.dp))

        Text("Progresso dos Hábitos", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        GeneralHabitsProgressLineChart(habits = habits)

        Spacer(Modifier.height(16.dp))

        Text("Progresso Semanal", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        WeeklyProgressPieChart(
            habits = habits,
            startOfWeek = startOfWeek,
            capAt100 = true
        )

        Spacer(Modifier.height(16.dp))
    }
}
