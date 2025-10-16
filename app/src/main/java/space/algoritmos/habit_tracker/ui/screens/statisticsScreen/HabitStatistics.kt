package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import space.algoritmos.habit_tracker.domain.model.Habit
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitStatistics(habit: Habit) {
    val streak = habit.streakCount()
    val maxStreak = habit.maxStreak()
    val totalProgress = habit.progress.values.sum()
    val avgProgress = habit.progress.values.average()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(habit.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        StatisticItem("Sequência Atual", "$streak dias")
        StatisticItem("Maior Sequência", "$maxStreak dias")
        StatisticItem("Progresso Total", "$totalProgress")
        StatisticItem("Média por Dia", "%.1f".format(avgProgress))
    }
}
