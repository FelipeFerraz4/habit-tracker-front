package space.algoritmos.habit_tracker.ui.screens.aboutScreen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import space.algoritmos.habit_tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_app_name), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = stringResource(R.string.about_app_version),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(stringResource(R.string.about_description))

            Text(stringResource(R.string.about_description_2))

            HorizontalDivider()

            Text(stringResource(R.string.about_objective_title), style = MaterialTheme.typography.titleLarge)

            Text("• ${stringResource(R.string.about_objective_1)}")
            Text("• ${stringResource(R.string.about_objective_2)}")
            Text("• ${stringResource(R.string.about_objective_3)}")

            HorizontalDivider()

            Text(stringResource(R.string.about_version_title), style = MaterialTheme.typography.titleLarge)

            Text(stringResource(R.string.about_version_description))

            HorizontalDivider()

            Text(stringResource(R.string.about_project_title), style = MaterialTheme.typography.titleLarge)

            Text(stringResource(R.string.about_project_description))

            HorizontalDivider()

            Text(stringResource(R.string.about_privacy_title), style = MaterialTheme.typography.titleLarge)

            TextButton(
                onClick = {
                    openUrl("https://bluefoxaquarismo.space/app/daily-habits/privacy-policy")
                }
            ) {
                Text("• ${stringResource(R.string.about_privacy_policy)}", fontSize = 16.sp)
            }

            TextButton(
                onClick = {
                    openUrl("https://bluefoxaquarismo.space/app/daily-habits/terms-of-use")
                }
            ) {
                Text("• ${stringResource(R.string.about_terms_of_use)}", fontSize = 16.sp)
            }

            HorizontalDivider()

            Text(stringResource(R.string.about_feedback_title), style = MaterialTheme.typography.titleLarge)

            Text(stringResource(R.string.about_feedback_description))

            HorizontalDivider()

            Text(stringResource(R.string.about_developer), style = MaterialTheme.typography.titleMedium)
        }
    }
}