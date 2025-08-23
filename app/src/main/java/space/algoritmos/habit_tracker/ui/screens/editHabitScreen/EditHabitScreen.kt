package space.algoritmos.habit_tracker.ui.screens.editHabitScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.TrackingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitScreen(
    habit: Habit,
    onSave: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf(habit.name) }
    var selectedColor by remember { mutableStateOf(habit.color) }
    var trackingMode by remember { mutableStateOf(habit.trackingMode) }
    var goalText by remember { mutableStateOf(habit.goal.toString()) }
    var showGoalField by remember { mutableStateOf(habit.trackingMode == TrackingMode.VALUE) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFFFFC107), Color(0xFFFF5722)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Editar hábito",
                            fontSize = 28.sp,
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
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar Hábito",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Hábito", fontSize = 18.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    modifier = Modifier
                        .fillMaxWidth(0.93f)
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text("Escolha a cor:", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color, CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == color) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = {
                        showGoalField = !showGoalField
                        trackingMode = if (showGoalField) TrackingMode.VALUE else TrackingMode.BINARY
                    },
                    border = BorderStroke(2.dp, Color.Gray),
                ) {
                    Text(if (showGoalField) "Remover meta" else "Adicionar meta", fontSize = 20.sp)
                }

                if (showGoalField) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = goalText,
                        onValueChange = { goalText = it },
                        label = { Text("Meta (ex: 20 páginas)", fontSize = 18.sp) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(fontSize = 22.sp),
                        modifier = Modifier
                            .fillMaxWidth(0.93f)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val goal = if (showGoalField) goalText.toIntOrNull() ?: 1 else 1
                        val updatedHabit = habit.copy(
                            name = name,
                            color = selectedColor,
                            trackingMode = trackingMode,
                            goal = goal
                        )
                        onSave(updatedHabit)
                    },
                    enabled = name.isNotBlank(),
                    border = BorderStroke(2.dp, Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Salvar", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.size(20.dp))
        }

        // AlertDialog de confirmação de exclusão
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        text = "Excluir Hábito",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Text(
                        text = "Você realmente deseja remover este hábito ?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Botão "Sim" à esquerda
                        Button(
                            onClick = {
                                onDelete(habit)
                                showDeleteDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Sim",
                                color = MaterialTheme.colorScheme.onError
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp)) // espaço entre os botões

                        // Botão "Não" à direita
                        OutlinedButton(
                            onClick = { showDeleteDialog = false },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Não")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }


    }
}
