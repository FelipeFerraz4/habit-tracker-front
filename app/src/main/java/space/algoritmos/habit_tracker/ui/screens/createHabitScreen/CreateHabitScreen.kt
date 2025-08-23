package space.algoritmos.habit_tracker.ui.screens.createHabitScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    onSave: (Habit) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color(0xFF4CAF50)) }
    var trackingMode by remember { mutableStateOf<TrackingMode?>(null) }
    var goalText by remember { mutableStateOf("") }
    var showGoalField by remember { mutableStateOf(false) }

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
                            text = "Habit Tracker",
                            fontSize = 32.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
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
                Text("Criar novo hábito", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(64.dp))

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

                Spacer(modifier = Modifier.height(44.dp))

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

                Spacer(modifier = Modifier.height(44.dp))

                OutlinedButton(
                    onClick = {
                        showGoalField = !showGoalField
                        trackingMode = if (showGoalField) TrackingMode.VALUE else TrackingMode.BINARY
                    },
                    border = BorderStroke(2.dp, Color.Gray),
                    modifier = Modifier
                        .padding(4.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val goal = if (showGoalField) goalText.toIntOrNull() ?: 1 else 1
                        val finalMode = if(showGoalField) TrackingMode.VALUE else TrackingMode.BINARY
                        val newHabit = Habit(
                            id = UUID.randomUUID(),
                            name = name,
                            color = selectedColor,
                            trackingMode = finalMode,
                            goal = goal
                        )
                        onSave(newHabit)
                    },
                    enabled = name.isNotBlank(),
                    border = BorderStroke(2.dp, Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Salvar", fontSize = 20.sp)
                }

                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(2.dp, Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Cancelar", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}