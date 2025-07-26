package space.algoritmos.habit_tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seus Hábitos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Ação para adicionar novo hábito
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Hábito")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bem-vindo ao Habit Tracker!", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Aqui você verá seus hábitos do dia.", fontSize = 16.sp)
        }
    }
}
