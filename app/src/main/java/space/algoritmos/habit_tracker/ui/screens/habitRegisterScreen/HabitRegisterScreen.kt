package space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.model.Habit
import space.algoritmos.habit_tracker.model.TrackingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitRegisterScreen(
    habit: Habit,
    onSave: (Float) -> Unit,
    onCancel: () -> Unit
) {
    var value by remember { mutableStateOf("") }

    var statusMode by remember {
        mutableStateOf(
            when (habit.trackingMode) {
                TrackingMode.BINARY -> "binary"
                TrackingMode.VALUE -> "value"
                TrackingMode.PERCENTAGE -> "percentage"
            }
        )
    }

    val label = when (habit.trackingMode) {
        TrackingMode.BINARY -> "Você completou este hábito hoje?"
        TrackingMode.VALUE -> "Quanto você completou sua meta (${habit.goal}) hoje?"
        TrackingMode.PERCENTAGE -> "Qual a porcentagem da sua meta (${habit.goal}) você concluída hoje?"
    }
    var selectedValue by remember { mutableStateOf<Float?>(null) }

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 32.sp,
                textAlign = TextAlign.Justify
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 24.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                when (statusMode) {
                    "binary" -> {
                        Spacer(modifier = Modifier.size(150.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 18.dp)
                        ) {
                            Button(
                                onClick = { selectedValue = 1f },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedValue == 1f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                ),
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(150.dp)

                            ) {
                                Text("Sim", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.size(20.dp))

                            Button(
                                onClick = { selectedValue = 0f },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedValue == 0f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                ),
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(150.dp)
                            ) {
                                Text("Não", fontSize = 20.sp)
                            }
                        }
                    }

                    "value" -> {
                        Spacer(modifier = Modifier.size(80.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            OutlinedTextField(
                                value = value,
                                onValueChange = {
                                    value = it
                                    selectedValue = it.toFloatOrNull()?.coerceAtLeast(0f) ?: 0f
                                },
                                label = {
                                    Text("Digite a quantidade (un)", fontSize = 20.sp)
                                },
                                textStyle = TextStyle( // centraliza o texto digitado
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .clip(RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                            )

                            Spacer(modifier = Modifier.size(50.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                Button(
                                    onClick = {
                                        val current = value.toFloatOrNull() ?: 0f
                                        val newValue = (current - 1).coerceAtLeast(0f) // evita negativo
                                        value = if (newValue % 1f == 0f) newValue.toInt().toString() else newValue.toString()
                                        selectedValue = newValue
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("-", fontSize = 24.sp)
                                }

                                Button(
                                    onClick = {
                                        val current = value.toFloatOrNull() ?: 0f
                                        val newValue = current + 1
                                        value = if (newValue % 1f == 0f) newValue.toInt().toString() else newValue.toString()
                                        selectedValue = newValue
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("+", fontSize = 24.sp)
                                }
                            }

                            Spacer(modifier = Modifier.size(24.dp))

                            OutlinedButton(
                                onClick = { statusMode = "percentage" },
                                modifier = Modifier.height(40.dp),
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
                            ) {
                                Text("Trocar para Porcentage")
                            }
                        }
                    }

                    "percentage" -> {
                        Spacer(modifier = Modifier.size(40.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Campo de entrada + botão salvar
                            OutlinedTextField(
                                value = if(value == "") {
                                    value
                                } else {
                                    "$value%"
                                },
                                onValueChange = {
                                    value = it
                                    selectedValue = it.toFloatOrNull()?.coerceIn(0f, 100f)?.div(100f) ?: 0f
                                },
                                label = {
                                    Text("Digite o valor (%)", fontSize = 20.sp)
                                },
                                textStyle = TextStyle( // Isso centraliza o texto digitado
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .clip(RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                            )

                            Spacer(modifier = Modifier.size(20.dp))
                            // LazyRow com 0 até 100%
                            val listState = rememberLazyListState(initialFirstVisibleItemIndex = 48)

                            LazyRow(
                                state = listState,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 1.dp)
                            ) {
                                items(101) { i -> // 0 até 100
                                    val percentFloat = i / 100f
                                    Button(
                                        onClick = {
                                            selectedValue = percentFloat
                                            value = i.toString()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedValue == percentFloat)
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onBackground
                                        ),
                                        modifier = Modifier
                                            .height(44.dp)
                                            .width(78.dp)
                                    ) {
                                        Text("$i", fontSize = 16.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.size(20.dp))
                            // Botões com valores mais comuns
                            val commonPercentages = listOf(10, 20, 25, 33, 50, 66, 75, 90, 100)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                commonPercentages.chunked(3).forEach { rowValues ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowValues.forEach { percent ->
                                            val percentFloat = percent / 100f
                                            Button(
                                                onClick = {
                                                    selectedValue = percentFloat
                                                    value = percent.toString()
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (selectedValue == percentFloat)
                                                        MaterialTheme.colorScheme.primary
                                                    else MaterialTheme.colorScheme.onBackground
                                                ),
                                                modifier = Modifier.weight(1f).height(45.dp)
                                            ) {
                                                Text("$percent%", fontSize = 18.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.size(10.dp))

                            OutlinedButton(
                                onClick = { statusMode = "value" },
                                modifier = Modifier
                                    .height(40.dp),
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
                            ) {
                                Text("Trocar para value")
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        val finalValue = when (habit.trackingMode) {
                            TrackingMode.BINARY -> selectedValue ?: return@Button
                            TrackingMode.PERCENTAGE -> {
                                val rawValue = value.toFloatOrNull() ?: return@Button
                                rawValue.coerceIn(0f, 100f) / 100f
                            }
                            TrackingMode.VALUE -> {
                                val rawValue = value.toFloatOrNull() ?: return@Button
                                (rawValue / habit.goal).coerceIn(0f, 1f)
                            }
                        }
                        onSave(finalValue)
                    },
                    enabled = when (habit.trackingMode) {
                        TrackingMode.BINARY -> selectedValue != null
                        else -> value.toFloatOrNull() != null
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .width(160.dp)
                ) {
                    Text("Salvar", fontSize = 24.sp)
                }

                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .height(56.dp)
                        .width(160.dp)
                ) {
                    Text("Cancelar", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.size(32.dp))
        }

    }
}
