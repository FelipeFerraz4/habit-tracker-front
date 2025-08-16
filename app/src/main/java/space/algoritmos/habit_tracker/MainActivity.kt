package space.algoritmos.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import space.algoritmos.habit_tracker.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val toggleTheme = { isDarkTheme = !isDarkTheme }

            // 1️⃣ Instanciar DatabaseHelper
            val dbHelper = remember { DatabaseHelper(this) }

            // 2️⃣ Criar DAO
            val habitDao = remember { HabitDao(dbHelper) }

            // 3️⃣ Criar Repository
            val habitRepository = remember { HabitRepository(habitDao) }

            MaterialTheme(
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                // Passar repository para o NavHost / Screens
                AppNavHost(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = toggleTheme,
                    habitRepository = habitRepository
                )
            }
        }
    }
}
