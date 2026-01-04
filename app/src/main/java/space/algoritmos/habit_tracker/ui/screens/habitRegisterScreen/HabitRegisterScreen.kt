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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import space.algoritmos.habit_tracker.R
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
    val today = LocalDate.now()

    var value by remember {
        mutableStateOf(
            when (habit.trackingMode) {
                TrackingMode.VALUE -> {
                    val progress = habit.progress[today] ?: 0
                    if (progress == 0) "" else progress.toString()
                }
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

    val label = when (habit.trackingMode) {
        TrackingMode.BINARY -> stringResource(id = R.string.register_habit_binary_label)
        TrackingMode.VALUE -> stringResource(id = R.string.register_habit_value_label, habit.goal)
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
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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

                    when (habit.trackingMode) {
                        TrackingMode.BINARY -> {
                            Spacer(modifier = Modifier.height(80.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 18.dp)
                            ) {
                                Button(
                                    onClick = { selectedValue = 1 },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedValue == 1) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = if (selectedValue == 1) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(150.dp)
                                ) {
                                    Text(stringResource(id = R.string.common_yes), fontSize = 20.sp)
                                }

                                Spacer(modifier = Modifier.size(20.dp))

                                Button(
                                    onClick = { selectedValue = 0 },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedValue == 0) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = if (selectedValue == 0) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(150.dp)
                                ) {
                                    Text(stringResource(id = R.string.common_no), fontSize = 20.sp)
                                }
                            }
                        }

                        TrackingMode.VALUE -> {
                            Spacer(modifier = Modifier.height(40.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                OutlinedTextField(
                                    value = value,
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) {
                                            value = it
                                            selectedValue = it.toIntOrNull()
                                        }
                                    },
                                    label = {
                                        Text(stringResource(id = R.string.register_habit_value_placeholder), fontSize = 20.sp)
                                    },
                                    placeholder = { Text("0") },
                                    textStyle = TextStyle(
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
                                            val newValue = (current - 1).coerceAtLeast(0)
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
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(id = R.string.cancel), fontSize = 24.sp)
                }

                Button(
                    onClick = {
                        val finalValue = when (habit.trackingMode) {
                            TrackingMode.BINARY -> selectedValue ?: 0
                            TrackingMode.VALUE -> value.toIntOrNull() ?: 0
                        }
                        onSave(finalValue)
                    },
                    enabled = when (habit.trackingMode) {
                        TrackingMode.BINARY -> selectedValue != null
                        else -> true
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(id = R.string.save), fontSize = 24.sp)
                }
            }
        }
    }
}
