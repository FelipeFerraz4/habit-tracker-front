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
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitRegisterScreen(
    habit: Habit,
    onSave: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var today = LocalDate.now()

    var value by remember {
        mutableStateOf(
            when (habit.trackingMode) {
                TrackingMode.VALUE -> (habit.progress[today] ?: 0).toString()
                TrackingMode.BINARY -> if ((habit.progress[today] ?: 0) > 0) "1" else "0"
            }
        )
    }

    var selectedValue by remember {
        mutableStateOf(
            when (habit.trackingMode) {
                TrackingMode.BINARY -> habit.progress[today] ?: 0
                TrackingMode.VALUE -> value.toIntOrNull()
            }
        )
    }

    var statusMode by remember {
        mutableStateOf(
            when (habit.trackingMode) {
                TrackingMode.BINARY -> "binary"
                TrackingMode.VALUE -> "value"
            }
        )
    }

    val label = when (habit.trackingMode) {
        TrackingMode.BINARY -> "Você completou este hábito hoje?"
        TrackingMode.VALUE -> "Quanto você completou sua meta (${habit.goal}) hoje?"
    }

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
                                onClick = { selectedValue = 1 },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedValue == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                ),
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(150.dp)

                            ) {
                                Text("Sim", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.size(20.dp))

                            Button(
                                onClick = { selectedValue = 0 },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedValue == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
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
                                    selectedValue = it.toIntOrNull()?.coerceAtLeast(0) ?: 0
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
                                        val current = value.toIntOrNull() ?: 0
                                        val newValue = (current - 1).coerceAtLeast(0) // evita negativo
                                        value = newValue.toString()
                                        selectedValue = newValue
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("-", fontSize = 24.sp)
                                }

                                Button(
                                    onClick = {
                                        val current = value.toIntOrNull() ?: 0
                                        val newValue = current + 1
                                        value = newValue.toString()
                                        selectedValue = newValue
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("+", fontSize = 24.sp)
                                }
                            }

                            Spacer(modifier = Modifier.size(24.dp))
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
                            TrackingMode.VALUE -> (value.toIntOrNull() ?: return@Button).coerceAtLeast(0)
                        }
                        onSave(finalValue)
                    },
                    enabled = when (habit.trackingMode) {
                        TrackingMode.BINARY -> selectedValue != null
                        else -> value.toIntOrNull() != null
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
