package space.algoritmos.habit_tracker.ui.screens.statisticsScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class) // Necessário para ExposedDropdownMenuBox
@Composable
fun HabitSelector(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // Use ExposedDropdownMenuBox para coordenar o TextField e o Menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        // Este é o campo que fica sempre visível. Usamos um OutlinedTextField read-only para
        // se parecer com um botão, como você queria.
        OutlinedTextField(
            value = selected,
            onValueChange = {}, // Deixe vazio pois não é editável
            textStyle = TextStyle(fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface),
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            // O modificador 'menuAnchor' é crucial. Ele diz ao Box qual
            // componente ancora o menu suspenso.
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)

        )

        // Este é o menu que aparece
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.graphicsLayer {
                shape = RoundedCornerShape(16.dp)
                clip = true
            }
        ) {

            options.forEach { option ->
                val isSelected = option == selected
                DropdownMenuItem(
                    text = { Text(option, color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp) },

                    leadingIcon = {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = "Selecionado")
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                )
            }
        }
    }
}
