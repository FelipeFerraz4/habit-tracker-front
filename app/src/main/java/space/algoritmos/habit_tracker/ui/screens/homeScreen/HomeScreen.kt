package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.model.Habit
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    habits: List<Habit>,
    onHabitClick: (Habit) -> Unit,
    streakCount: Int,

    // Drawer + tema + conta
    isLoggedIn: Boolean,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onStatsClick: () -> Unit,

    // Ações de botão
    onSyncClick: () -> Unit,
    onAddHabitClick: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val maxStreak by remember(habits) { mutableIntStateOf(calculateMaxStreak(habits)) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HabitDrawerContent(
                isLoggedIn = isLoggedIn,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onLoginClick = {
                    onLoginClick()
                    coroutineScope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    onLogoutClick()
                    coroutineScope.launch { drawerState.close() }
                },
                onStatsClick = {
                    onStatsClick()
                    coroutineScope.launch { drawerState.close() }
                },
                onAddHabitClick = {
                    onAddHabitClick()
                    coroutineScope.launch { drawerState.close() }
                },
                onSyncClick = {
                    onSyncClick()
                    coroutineScope.launch { drawerState.close() }
                },
                drawerState = drawerState
            )
        }
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
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥", fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "$streakCount dias", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏆", fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Máx: $maxStreak", fontSize = 20.sp)
                    }
                }

                MonthlyHeatmap(
                    habits = habits,
                    currentMonth = currentMonth,
                    onPreviousMonth = {
                        currentMonth = currentMonth.minusMonths(1)
                    },
                    onNextMonth = {
                        currentMonth = currentMonth.plusMonths(1)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
