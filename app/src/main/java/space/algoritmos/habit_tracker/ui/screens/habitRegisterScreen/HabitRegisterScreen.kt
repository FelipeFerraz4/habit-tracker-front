package space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen

import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitRegisterScreen(
    habit: Habit,
    onSave: (LocalDate, Float) -> Unit,
    onCancel: () -> Unit
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val dateFormatter = remember(configuration) {
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT)
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var value by remember(habit.id, selectedDate) {
        mutableStateOf(
            habit.progress[selectedDate]?.done?.toInputString() ?: ""
        )
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        },
        selectedDate.year,
        selectedDate.monthValue - 1,
        selectedDate.dayOfMonth
    )

    val label = stringResource(
        R.string.register_habit_value_label,
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = habit.name,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = label,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedButton(
                    onClick = {
                        datePickerDialog.show()
                    }
                ) {
                    Text(
                        text = stringResource(
                            R.string.register_habit_selected_date,
                            selectedDate.format(dateFormatter)
                        ),
                        fontSize = 18.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedButton(
                    onClick = onCancel,
                    border = BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(
                        stringResource(id = R.string.cancel),
                        fontSize = 22.sp
                    )
                }

                Button(
                    onClick = {
                        onSave(
                            selectedDate,
                            value.toFloatOrNull() ?: 0f
                        )
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(
                        stringResource(id = R.string.save),
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}