package space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen.utils.toInputString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitRegisterScreen(
    habit: Habit,
    onSave: (Float) -> Unit,
    onCancel: () -> Unit
) {
    val today = LocalDate.now()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var value by remember(habit.id, today) {
        mutableStateOf(
            habit.progress[today]?.done?.toInputString() ?: ""
        )
    }

    val label = stringResource(
        R.string.register_habit_value_label_with_unit,
        habit.goal,
        habit.unit
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
                            text = stringResource(id = R.string.app_name),
                            fontSize = 32.sp,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Nome do hábito
            Text(
                text = habit.name,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Label com meta + unidade
            Text(
                text = label,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            if (it.matches(Regex("""\d*\.?\d*"""))) {
                                value = it
                            }
                        },
                        label = {
                            Text(
                                text = habit.unit,
                                fontSize = 18.sp
                            )
                        },
                        placeholder = { Text("0") },
                        textStyle = TextStyle(
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Botões + / -
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Button(
                            onClick = {
                                val current = value.toFloatOrNull() ?: 0f
                                val newValue = (current - 1f).coerceAtLeast(0f)
                                value = newValue.toInputString()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("-", fontSize = 24.sp)
                        }

                        Button(
                            onClick = {
                                val current = value.toFloatOrNull() ?: 0f
                                val newValue = current + 1f
                                value = newValue.toInputString()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+", fontSize = 24.sp)
                        }
                    }
                }
            }

            // Ações
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(id = R.string.cancel), fontSize = 22.sp)
                }

                Button(
                    onClick = {
                        onSave(value.toFloatOrNull() ?: 0f)
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(stringResource(id = R.string.save), fontSize = 22.sp)
                }
            }
        }
    }
}
