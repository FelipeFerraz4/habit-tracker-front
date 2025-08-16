package space.algoritmos.habit_tracker.ui.screens.habitScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habit: Habit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    onSyncClick: () -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var habitState by remember { mutableStateOf(habit) }

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título centralizado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 32.sp
                )
            }

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
                    Text(text = "${habit.streakCount()} dias", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏆", fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Máx: ${habit.maxStreak()}", fontSize = 20.sp)
                }
            }

            Heatmap(
                habit = habitState,
                currentMonth = currentMonth,
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )

            Spacer(modifier = Modifier.weight(1f)) // empurra o botão para o final

            // 📅 Botão de registrar
            Button(
                onClick = {
                    // 1️⃣ Registrar o progresso real no banco/repositório
                    val today = LocalDate.now()
                    val newValue = habit.progress[today] ?: 0f

                    onRegisterClick() // aqui você chama a função que salva no HabitRepository

                    // 2️⃣ Atualizar apenas o progresso do dia atual no estado local
                    habitState = habitState.copy(
                        progress = habitState.progress.toMutableMap().apply {
                            put(today, newValue)
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = habitState.color,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Registrar Hábito", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}
