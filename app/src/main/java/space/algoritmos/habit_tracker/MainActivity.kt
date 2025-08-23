package space.algoritmos.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.*
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.data.local.ThemeMode
import space.algoritmos.habit_tracker.data.local.ThemePreferences
import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import space.algoritmos.habit_tracker.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themePrefs = ThemePreferences(this)

        setContent {
            val themeMode by themePrefs.themeFlow.collectAsState(initial = ThemeMode.DARK)
            val coroutineScope = rememberCoroutineScope() // Scope do Compose

            val toggleTheme: () -> Unit = {
                val newTheme = if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
                coroutineScope.launch {
                    themePrefs.saveTheme(newTheme)
                }
            }

            val dbHelper = remember { DatabaseHelper(this) }
            val habitDao = remember { HabitDao(dbHelper) }
            val habitRepository = remember { HabitRepository(habitDao) }

            MaterialTheme(
                colorScheme = if (themeMode == ThemeMode.DARK) darkColorScheme() else lightColorScheme()
            ) {
                AppNavHost(
                    isDarkTheme = themeMode == ThemeMode.DARK,
                    onToggleTheme = toggleTheme,
                    habitRepository = habitRepository
                )
            }
        }
    }
}
