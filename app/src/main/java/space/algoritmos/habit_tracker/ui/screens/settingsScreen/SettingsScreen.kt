package space.algoritmos.habit_tracker.ui.screens.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.algoritmos.habit_tracker.R
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentHeatmapColor: Color,
    onSaveSettings: (Color) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedColor by remember { mutableStateOf(currentHeatmapColor) }
    var customColor by remember { mutableStateOf(currentHeatmapColor) }
    var showColorPicker by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFFFFC107), Color(0xFFFF5722)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    onSaveSettings(selectedColor)
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text(stringResource(id = R.string.save), fontSize = 20.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.settings_heatmap_color_label),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espaço vertical entre as linhas de cores
            ) {
                val midPoint = ceil(colors.size / 2.0).toInt()
                val firstRowColors = colors.subList(0, midPoint)
                val secondRowColors = colors.subList(midPoint, colors.size)

                // ** Primeira Linha de Cores **
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    firstRowColors.forEach { color ->
                        ColorCircle(
                            color = color,
                            isSelected = selectedColor == color,
                            onClick = {
                                selectedColor = color
                                customColor = color
                            }
                        )
                    }
                }

                // ** Segunda Linha de Cores + Botão Customizável **
                Row(
                    modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    secondRowColors.forEach { color ->
                        ColorCircle(
                            color = color,
                            isSelected = selectedColor == color,
                            onClick = {
                                selectedColor = color
                                customColor = color
                            }
                        )
                    }

                    // Botão de cor personalizada
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(customColor, CircleShape)
                            .border(
                                width = 3.dp,
                                color = if (!colors.contains(selectedColor))
                                    MaterialTheme.colorScheme.onBackground else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable { showColorPicker = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
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
                            Text(stringResource(R.string.confirm))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showColorPicker = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                    },
                    title = { Text(stringResource(R.string.choose_custom_color)) },
                    text = {
                        // Conteúdo do seletor de cor com sliders
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(rememberScrollState())) {
                            Box(modifier = Modifier.size(80.dp).background(customColor, CircleShape).border(2.dp, Color.Gray, CircleShape))
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
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

/**
 * Composable extraído para representar um círculo de cor, evitando repetição de código.
 */
@Composable
private fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, CircleShape)
            .border(
                width = 3.dp,
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}
