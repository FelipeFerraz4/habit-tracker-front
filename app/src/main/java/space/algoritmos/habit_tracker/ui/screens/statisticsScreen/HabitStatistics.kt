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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
// Importações para i18n
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.HabitDailyProgressChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.WeekdayPatternBarChartSingle
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.WeeklyStatusDonut
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun HabitStatistics(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val streak = habit.streakCount()
    val maxStreak = habit.maxStreak()
    val totalProgress = habit.progress.values.sum()
    val avgProgress = habit.progress.values.average()

    val resources = LocalContext.current.resources
    val streakText = resources.getQuantityString(R.plurals.streak_days, streak, streak)
    val maxStreakText = resources.getQuantityString(R.plurals.streak_days, maxStreak, maxStreak)
    val daysRegisteredText = resources.getQuantityString(R.plurals.streak_days, habit.progress.size, habit.progress.size)

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
        Text(habit.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        StatisticItem(stringResource(id = R.string.stats_current_streak), streakText)
        StatisticItem(stringResource(id = R.string.stats_max_streak), maxStreakText)
        StatisticItem(stringResource(id = R.string.stats_total_progress), totalProgress.toString())
        StatisticItem(stringResource(id = R.string.stats_total_days_registered), daysRegisteredText)
        StatisticItem(
            stringResource(id = R.string.stats_avg_progress_per_day),
            // Formata o número decimal de acordo com o Locale do usuário (ponto vs vírgula)
            String.format(Locale.getDefault(), "%.1f", avgProgress)
        )

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_habits_progress_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        HabitDailyProgressChart(habit)

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_weekday_progress_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        WeekdayPatternBarChartSingle(habit)

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_weekly_progress_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        WeeklyStatusDonut(habit, startOfWeek = startOfWeek)

        Spacer(modifier = Modifier.height(16.dp))
    }
}
