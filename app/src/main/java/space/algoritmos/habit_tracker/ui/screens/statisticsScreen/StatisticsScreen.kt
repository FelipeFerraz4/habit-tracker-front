package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Importações para i18n
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    habits: List<Habit>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val generalOptionText = stringResource(id = R.string.stats_general_option)
    var selectedOption by remember { mutableStateOf(generalOptionText) }
    val habitNames = remember(habits, generalOptionText) {
        listOf(generalOptionText) + habits.map { it.name }
    }
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
                            text = stringResource(id = R.string.app_name),
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
                            contentDescription = stringResource(id = R.string.back_button_description),
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
            if (selectedOption == generalOptionText) {
                GeneralStatistics(habits)
            } else if (selectedHabit != null) {
                HabitStatistics(selectedHabit)
            }
        }
    }
}
