package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import space.algoritmos.habit_tracker.domain.model.Habit
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.HabitDailyProgressChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.HabitStreakLineChart

@Composable
fun HabitStatistics(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val streak = habit.streakCount()
    val maxStreak = habit.maxStreak()
    val totalProgress = habit.progress.values.sum()
    val avgProgress = habit.progress.values.average()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(habit.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        StatisticItem("Sequência Atual", "$streak dias")
        StatisticItem("Maior Sequência", "$maxStreak dias")
        StatisticItem("Progresso Total", "$totalProgress")
        StatisticItem("Média por Dia", "%.1f".format(avgProgress))

        Spacer(Modifier.height(16.dp))

        // Progresso diário do hábito
        HabitDailyProgressChart(habit)

        Spacer(Modifier.height(12.dp))

        // Streak ao longo do tempo
        HabitStreakLineChart(habit)
    }
}
