package space.algoritmos.habit_tracker.ui.screens.createHabitScreen

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
// Adicione a importação para stringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R // <<-- IMPORTANTE: Importe o R do seu pacote
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
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
    var goalText by remember { mutableStateOf("") }
    var showGoalField by remember { mutableStateOf(true) }

    val colors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFFFFC107), Color(0xFFFF5722)
    )

    var showColorPicker by remember { mutableStateOf(false) }
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
                            text = stringResource(id = R.string.app_name), // MODIFICADO
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.create_new_habit), // MODIFICADO
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.habit_name_label), fontSize = 18.sp) }, // MODIFICADO
                    textStyle = TextStyle(fontSize = 22.sp),
                    modifier = Modifier
                        .fillMaxWidth(0.93f)
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                )

                Spacer(modifier = Modifier.height(44.dp))

                Text(
                    text = stringResource(id = R.string.choose_color), // MODIFICADO
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
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
                                    customColor = color
                                }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(customColor, CircleShape)
                            .border(
                                width = 2.dp,
                                color = if (!colors.contains(selectedColor)) MaterialTheme.colorScheme.onBackground else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable { showColorPicker = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                if (showColorPicker) {
                    AlertDialog(
                        onDismissRequest = { showColorPicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                selectedColor = customColor
                                showColorPicker = false
                            }) {
                                Text(stringResource(id = R.string.confirm)) // MODIFICADO
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showColorPicker = false }) {
                                Text(stringResource(id = R.string.cancel)) // MODIFICADO
                            }
                        },
                        title = { Text(stringResource(id = R.string.choose_custom_color)) }, // MODIFICADO
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                // ... (o conteúdo do seletor de cores não tem texto para traduzir)
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(customColor, CircleShape)
                                        .border(2.dp, Color.Gray, CircleShape)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                var r by remember { mutableFloatStateOf(customColor.red) }
                                var g by remember { mutableFloatStateOf(customColor.green) }
                                var b by remember { mutableFloatStateOf(customColor.blue) }
                                fun updateColor() { customColor = Color(r, g, b) }
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

                Spacer(modifier = Modifier.height(44.dp))
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.define_habit_goal), // MODIFICADO
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
                    label = { Text(stringResource(id = R.string.goal_label)) }, // MODIFICADO
                    placeholder = { Text(stringResource(id = R.string.goal_placeholder)) }, // MODIFICADO
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Spacer(modifier = Modifier.height(30.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton( // Adicionei o botão de cancelar aqui, estava faltando
                    onClick = onCancel,
                    border = BorderStroke(2.dp, Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(stringResource(id = R.string.cancel), fontSize = 20.sp) // MODIFICADO
                }

                Button(
                    onClick = {
                        val goal = if (showGoalField) goalText.toIntOrNull() ?: 1 else 1
                        val finalMode = if (showGoalField) TrackingMode.VALUE else TrackingMode.BINARY
                        val newHabit = Habit(
                            id = UUID.randomUUID(),
                            name = name,
                            color = selectedColor,
                            trackingMode = finalMode,
                            status = HabitStatus.ACTIVE,
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
                    Text(stringResource(id = R.string.save), fontSize = 20.sp) // MODIFICADO
                }
            }
        }
    }
}
