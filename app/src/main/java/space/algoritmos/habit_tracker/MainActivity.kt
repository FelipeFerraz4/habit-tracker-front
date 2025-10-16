package space.algoritmos.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.data.local.ThemeMode
import space.algoritmos.habit_tracker.data.local.ThemePreferences
import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import space.algoritmos.habit_tracker.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
//        deleteDatabase("habit_tracker_database.db")

        val themePrefs = ThemePreferences(this)

        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.DARK) }
            var isThemeReady by remember { mutableStateOf(false) }

            splashScreen.setKeepOnScreenCondition { !isThemeReady }

            LaunchedEffect(Unit) {
                themeMode = themePrefs.themeFlow.first()
                isThemeReady = true
            }

            if (isThemeReady) {
                val coroutineScope = rememberCoroutineScope()

                val toggleTheme: () -> Unit = {
                    val newTheme = if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
                    coroutineScope.launch {
                        themePrefs.saveTheme(newTheme)
                        themeMode = newTheme
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
}
