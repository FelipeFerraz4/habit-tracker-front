package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.model.Habit

val dates = getLastAndNextNDaysDates(8, 3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit = {},
    onSyncClick: () -> Unit = {},
    onAddHabitClick: () -> Unit = {},
    habits: List<Habit>,
    onHabitClick: (Habit) -> Unit,
    streakCount: Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Habit Tracker",
                            fontSize = 32.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSyncClick) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sincronizar",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabitClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Hábito"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 🔥 Streak
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "🔥", fontSize = 36.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "$streakCount dias de sequência", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 📅 Heatmap
            Text(
                text = "Progresso recente:",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            HabitsHeatmap(habits = habits, dates = dates)

            // ✅ Lista de hábitos
            Text(text = "Seus hábitos:", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(habits) { habit ->
                    Card(
                        onClick = { onHabitClick(habit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = habit.color)
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                habit.name,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
