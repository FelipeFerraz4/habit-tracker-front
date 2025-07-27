package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.model.Habit

@Composable
fun HabitsHeatmap(habits: List<Habit>, dates: List<String>) {
    Column {
        habits.forEach { habit ->
            Text(
                text = habit.name,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                dates.forEach { date ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(2.dp)
                            .background(
                                color = if (habit.wasCompletedOn(date)) habit.color else Color(0xFFE0E0E0),
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
