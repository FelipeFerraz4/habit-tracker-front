package space.algoritmos.habit_tracker.ui.screens.habitScreen

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
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Heatmap(
    habit: Habit,
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
                            val progress = habit.progressOn(date)

                            val ratio = (progress.toFloat() / habit.goal).coerceIn(0f, 1f)
                            val color = if (progress == 0) {
                                Color.LightGray.copy(alpha = 0.3f)
                            } else {
                                habit.color.copy(alpha = ratio)
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
