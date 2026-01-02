package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
// Importações para i18n
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.dailyProgress
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthlyHeatmap(
    habits: List<Habit>,
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabeçalho com o mês e botões
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.heatmap_previous_month)
                )

            }
            Text(
                text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercaseChar() } + " ${currentMonth.year}",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.heatmap_next_month)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Grade de dias do mês
        Column(horizontalAlignment = Alignment.Start) {
            for (weekStart in 1..daysInMonth step 7) {
                Row {
                    for (dayOffset in 0..6) {
                        val day = weekStart + dayOffset
                        if (day <= daysInMonth) {
                            val date = currentMonth.atDay(day)
                            val progress = dailyProgress(date, habits)

                            val color = if (progress == 0f) {
                                Color.LightGray.copy(alpha = 0.3f)
                            } else {
                                Color(0xFF2196F3).copy(alpha = progress)
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(4.dp)
                                    .background(
                                        color = color,
                                        shape = MaterialTheme.shapes.small
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }
    }
}
