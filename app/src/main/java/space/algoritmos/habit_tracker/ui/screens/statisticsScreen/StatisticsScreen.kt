package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.domain.model.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    habits: List<Habit>, // lista vinda do ViewModel
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf("Geral") }
    val habitNames = listOf("Geral") + habits.map { it.name }
    val selectedHabit = habits.find { it.name == selectedOption }

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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔽 Dropdown de seleção
            HabitSelector(
                options = habitNames,
                selected = selectedOption,
                onSelectedChange = { selectedOption = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔽 Estatísticas
            if (selectedOption == "Geral") {
                GeneralStatistics(habits)
            } else if (selectedHabit != null) {
                HabitStatistics(selectedHabit)
            }
        }
    }
}
