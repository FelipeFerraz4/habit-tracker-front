package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Importações para i18n
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateCombinedStreak
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateMaxStreak
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.DailyHabitCountBarChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.GeneralHabitsProgressLineChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.WeekdayPatternBarChartMulti
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph.WeeklyProgressPieChart
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.utils.calculateAverageProgressPercent
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun GeneralStatistics(
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    val totalHabits = habits.size
    val totalDays = habits.sumOf { it.progress.size }
    val combinedStreak = calculateCombinedStreak(habits, LocalDate.now())
    val maxStreak = calculateMaxStreak(habits)

    val resources = LocalContext.current.resources
    val combinedStreakText = resources.getQuantityString(R.plurals.streak_days, combinedStreak, combinedStreak)
    val maxStreakText = resources.getQuantityString(R.plurals.streak_days, maxStreak, maxStreak)

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
            text = stringResource(id = R.string.stats_general_title), // MODIFICADO
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        StatisticItem(stringResource(id = R.string.stats_total_habits), totalHabits.toString())
        StatisticItem(stringResource(id = R.string.stats_total_days_registered), totalDays.toString())
        StatisticItem(stringResource(id = R.string.stats_current_streak), combinedStreakText)
        StatisticItem(stringResource(id = R.string.stats_max_streak), maxStreakText)
        StatisticItem(
            stringResource(id = R.string.stats_avg_progress_per_day),
            // Formata o número decimal de acordo com o Locale do usuário (ponto vs vírgula)
            String.format(Locale.getDefault(), "%.1f%%", calculateAverageProgressPercent(habits))
        )

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_habits_completed_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        DailyHabitCountBarChart(habits)

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_habits_progress_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        GeneralHabitsProgressLineChart(habits = habits)

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_weekday_progress_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        WeekdayPatternBarChartMulti(habits)

        Spacer(Modifier.height(24.dp))

        Text(stringResource(id = R.string.stats_weekly_progress_chart_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        WeeklyProgressPieChart(
            habits = habits,
            startOfWeek = startOfWeek,
            capAt100 = true
        )

        Spacer(Modifier.height(16.dp))
    }
}
