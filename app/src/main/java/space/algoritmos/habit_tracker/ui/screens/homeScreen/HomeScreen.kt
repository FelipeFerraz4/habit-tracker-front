package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateCombinedStreak
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.calculateMaxStreak
import space.algoritmos.habit_tracker.ui.screens.homeScreen.utils.dailyProgress
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    habits: List<Habit>,
    heatmapColor: Color,
    onHabitClick: (Habit) -> Unit,

    // Drawer + tema + conta
    isLoggedIn: Boolean,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onStatsClick: () -> Unit,

    // Ações de botão
    onSyncClick: () -> Unit,
    onAddHabitClick: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val maxStreak by remember(habits) {
        mutableIntStateOf(calculateMaxStreak(habits))
    }
    val streakCount by remember(habits) {
        mutableIntStateOf(calculateCombinedStreak(habits))
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf("") }

    val isDoneToday = dailyProgress(habits = habits) > 0f

    val streakLevel = when {
        isDoneToday -> 3
        streakCount > 0 -> 2
        else -> 1
    }

    val streakIcon = when (streakLevel) {
        3 -> Icons.Filled.LocalFireDepartment
        2 -> Icons.Filled.LocalFireDepartment
        else -> Icons.Filled.AcUnit
    }

    val streakColor = when (streakLevel) {
        3 -> Color(0xFFFF6D00) // laranja intenso
        2 -> Color(0xFF42A5F5) // fogo azulado
        else -> Color(0xFF81D4FA) // azul gelo
    }

    if (showDialog) {
        ComingSoonDialog(
            onDismiss = { showDialog = false },
            onType = dialogType
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HabitDrawerContent(
                isLoggedIn = isLoggedIn,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onLoginClick = {
                    dialogType = "login"
                    showDialog = true
                    coroutineScope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    dialogType = "logout"
                    showDialog = true
                    coroutineScope.launch { drawerState.close() }
                },
                onSettingsClick = { onSettingsClick() },
                onStatsClick = {
                    onStatsClick()
                    coroutineScope.launch { drawerState.close() }
                },
                onAddHabitClick = {
                    onAddHabitClick()
                    coroutineScope.launch { drawerState.close() }
                },
                onSyncClick = {
                    dialogType = "sync"
                    showDialog = true
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
                                text = stringResource(id = R.string.app_name),
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
                                contentDescription = stringResource(id = R.string.home_menu_description),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            dialogType = "sync"
                            showDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = stringResource(id = R.string.home_sync_description),
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
                        contentDescription = stringResource(id = R.string.home_add_habit_description)
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 🔥 Streak
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                imageVector = streakIcon,
                                contentDescription = null,
                                tint = streakColor,
                                modifier = Modifier.size(44.dp) // ícone maior
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = LocalContext.current.resources.getQuantityString(
                                    R.plurals.streak_days,
                                    streakCount,
                                    streakCount
                                ),
                                fontSize = 20.sp
                            )
                        }


                        Spacer(modifier = Modifier.weight(1f))

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = Color(0xFFFFC107), // dourado
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = stringResource(
                                    id = R.string.streak_max,
                                    maxStreak
                                ),
                                fontSize = 20.sp
                            )
                        }

                    }
                }

                // Heatmap
                item {
                    MonthlyHeatmap(
                        habits = habits,
                        currentMonth = currentMonth,
                        onPreviousMonth = {
                            currentMonth = currentMonth.minusMonths(1)
                        },
                        onNextMonth = {
                            currentMonth = currentMonth.plusMonths(1)
                        },
                        heatmapColor = heatmapColor
                    )
                }

                // Título da lista
                item {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(id = R.string.home_your_habits_title), fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Lista de hábitos
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
