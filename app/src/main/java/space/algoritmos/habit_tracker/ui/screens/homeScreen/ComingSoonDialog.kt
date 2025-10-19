package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ComingSoonDialog(
    onDismiss: () -> Unit,
    onType: String
) {
    val message = when (onType) {
        "login" -> "O login estará disponível na próxima versão do aplicativo."
        "logout" -> "O logout estará disponível na próxima versão do aplicativo."
        "sync" -> "A sincronização com a nuvem estará disponível na próxima versão."
        else -> "Esta funcionalidade estará disponível na próxima versão do aplicativo."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Em breve!") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text("OK", color = MaterialTheme.colorScheme.background)
            }
        }
    )
}

