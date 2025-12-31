package space.algoritmos.habit_tracker.ui.screens.editHabitScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    var goalText by remember { mutableStateOf(habit.goal.toString()) }
    var showGoalField by remember { mutableStateOf(habit.trackingMode == TrackingMode.VALUE) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFFFFC107), Color(0xFFFF5722)
    )

    // ===== ESTADOS PARA O SELETOR DE COR PERSONALIZADA =====
    var showColorPicker by remember { mutableStateOf(false) }
    // Inicializa 'customColor' com a cor selecionada (que vem do hábito)
    var customColor by remember { mutableStateOf(selectedColor) }

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
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

                // ===== Escolha de cor (MODIFICADO) =====
                Text("Escolha a cor:", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(color, CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == color) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedColor = color
                                    // Atualiza customColor também, para que o picker comece da última cor selecionada
                                    customColor = color
                                }
                        )
                    }

                    // Botão de cor personalizada
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(customColor, CircleShape)
                            .border(
                                width = 2.dp,
                                // Destaca se a cor selecionada não for uma das predefinidas
                                color = if (!colors.contains(selectedColor)) MaterialTheme.colorScheme.onBackground else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable { showColorPicker = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }


                // ===== Dialog do seletor de cor personalizada (AGORA RESPONSIVO) =====
                if (showColorPicker) {
                    AlertDialog(
                        onDismissRequest = { showColorPicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                selectedColor = customColor
                                showColorPicker = false
                            }) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showColorPicker = false }) {
                                Text("Cancelar")
                            }
                        },
                        title = { Text("Escolha uma cor personalizada") },
                        text = {
                            // Adicionamos uma Column rolável aqui
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.verticalScroll(rememberScrollState()) // <<--- ESSA É A MUDANÇA
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(customColor, CircleShape)
                                        .border(2.dp, Color.Gray, CircleShape)
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Sliders RGB
                                var r by remember { mutableFloatStateOf(customColor.red) }
                                var g by remember { mutableFloatStateOf(customColor.green) }
                                var b by remember { mutableFloatStateOf(customColor.blue) }

                                fun updateColor() {
                                    customColor = Color(r, g, b)
                                }

                                listOf("R" to r, "G" to g, "B" to b).forEach { (label, value) ->
                                    Text("$label: ${(value * 255).toInt()}")
                                    Slider(
                                        value = value,
                                        onValueChange = {
                                            when (label) {
                                                "R" -> r = it
                                                "G" -> g = it
                                                "B" -> b = it
                                            }
                                            updateColor()
                                        },
                                        valueRange = 0f..1f,
                                        modifier = Modifier.fillMaxWidth(0.9f)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Defina a meta para este hábito:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = goalText,
                    onValueChange = { goalText = it },
                    label = { Text("Meta (ex: 20 páginas, 3000 ml, etc.)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.height(20.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val goal = if (showGoalField) goalText.toIntOrNull() ?: 1 else 1
                        val finalMode = if (showGoalField) TrackingMode.VALUE else TrackingMode.BINARY
                        val updatedHabit = habit.copy(
                            name = name,
                            color = selectedColor,
                            trackingMode = finalMode,
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
