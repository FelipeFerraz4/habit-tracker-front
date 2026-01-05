package space.algoritmos.habit_tracker.ui.screens.createHabitScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    onSave: (Habit) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color(0xFF4CAF50)) }
    var goalText by remember { mutableStateOf("") }
    val defaultUnit = stringResource(id = R.string.default_unit)

    val colors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFFFFC107), Color(0xFFFF5722)
    )

    var showColorPicker by remember { mutableStateOf(false) }
    var customColor by remember { mutableStateOf(selectedColor) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
                .padding(12.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = R.string.create_new_habit),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.habit_name_label), fontSize = 18.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    modifier = Modifier
                        .fillMaxWidth(0.93f)
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                )

                Spacer(modifier = Modifier.height(44.dp))

                Text(
                    text = stringResource(id = R.string.choose_color),
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
                                Text(stringResource(id = R.string.confirm))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showColorPicker = false }) {
                                Text(stringResource(id = R.string.cancel))
                            }
                        },
                        title = { Text(stringResource(id = R.string.choose_custom_color)) },
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
                    text = stringResource(id = R.string.define_habit_goal),
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
                    label = { Text(stringResource(id = R.string.goal_label)) },
                    placeholder = { Text(stringResource(id = R.string.goal_placeholder)) },
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

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text(stringResource(id = R.string.unit_label)) },
                    placeholder = { Text(stringResource(id = R.string.unit_placeholder)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

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
                    Text(stringResource(id = R.string.cancel), fontSize = 20.sp)
                }

                Button(
                    onClick = {
                        val goal = goalText.toFloatOrNull() ?: 1f
                        val newHabit = Habit(
                            id = UUID.randomUUID(),
                            name = name,
                            color = selectedColor,
                            status = HabitStatus.ACTIVE,
                            unit = unit.ifBlank { defaultUnit },
                            goal = goal
                        )
                        onSave(newHabit)
                    },
                    enabled = name.isNotBlank() && goalText.isNotBlank() && unit.isNotBlank(),
                    border = BorderStroke(2.dp, Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(stringResource(id = R.string.save), fontSize = 20.sp)
                }
            }
        }
    }
}
