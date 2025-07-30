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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.model.Habit
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

fun dailyProgress(date: LocalDate, habits: List<Habit>): Float {
    if (habits.isEmpty()) return 0f
    val total = habits.fold(0f) { acc, habit -> acc + habit.progressOn(date) }
    return (total / habits.size).coerceIn(0f, 1f)
}

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
                    contentDescription = "Mês anterior"
                )

            }
            Text(
                text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
                    .replaceFirstChar { it.uppercaseChar() } + " ${currentMonth.year}",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.size(32.dp),
                    contentDescription = "Próximo mês"
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
                            Spacer(modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }
    }
}
