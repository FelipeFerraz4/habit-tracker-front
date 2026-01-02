package space.algoritmos.habit_tracker.ui.screens.homeScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import space.algoritmos.habit_tracker.R

@Composable
fun ComingSoonDialog(
    onDismiss: () -> Unit,
    onType: String
) {
    val message = when (onType) {
        "login" -> stringResource(id = R.string.coming_soon_login_message)
        "logout" -> stringResource(id = R.string.coming_soon_logout_message)
        "sync" -> stringResource(id = R.string.coming_soon_sync_message)
        else -> stringResource(id = R.string.coming_soon_default_message)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.coming_soon_title)) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    stringResource(id = R.string.coming_soon_ok_button),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

