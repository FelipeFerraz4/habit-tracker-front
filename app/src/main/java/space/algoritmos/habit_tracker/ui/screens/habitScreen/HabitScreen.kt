package space.algoritmos.habit_tracker.ui.screens.habitScreen

import androidx.compose.foundation.layout.*
// Importações para a funcionalidade de rolagem
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
    onEditClick: () -> Unit
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
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
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
        // Column principal que organiza a tela em "conteúdo" e "botão de ação"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Column interna e rolável para todo o conteúdo de detalhe
            Column(
                modifier = Modifier
                    .weight(1f) // Ocupa todo o espaço disponível, empurrando o botão para baixo
                    .verticalScroll(rememberScrollState()), // <<-- MUDANÇA PRINCIPAL
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os itens
            ) {
                // Título
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 32.sp
                )

                // 🔥 Streak
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround, // Usa SpaceAround para melhor distribuição
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥", fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "${habit.streakCount()} dias", fontSize = 20.sp)
                    }

                    // Spacer(modifier = Modifier.weight(1f)) // Não é mais necessário com SpaceAround

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

                // Adiciona um spacer no final da rolagem para não colar no botão
                Spacer(modifier = Modifier.height(16.dp))
            }

            // O Spacer com weight(1f) foi removido daqui e a lógica movida para a Column interna

            // 📅 Botão de registrar - Fixo na parte inferior
            Button(
                onClick = {
                    val today = LocalDate.now()
                    val newValue = habit.progress[today] ?: 0

                    onRegisterClick()

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Registrar Hábito", fontSize = 24.sp)
            }
        }
    }
}
