package space.algoritmos.habit_tracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.WorkManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.data.local.preferences.ThemeMode
import space.algoritmos.habit_tracker.data.local.preferences.ThemePreferences
import space.algoritmos.habit_tracker.data.local.dao.HabitDao
import space.algoritmos.habit_tracker.data.mocks.MockDataGenerator
import space.algoritmos.habit_tracker.data.repository.HabitRepository
import space.algoritmos.habit_tracker.navigation.AppNavHost
// Importa o agendador
import space.algoritmos.habit_tracker.notifications.scheduleDailyNotification
import space.algoritmos.habit_tracker.notifications.scheduleHabitReminder

class MainActivity : ComponentActivity() {

    // Launcher para pedir permissão (Android 13+)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val workManager = WorkManager.getInstance(this)
                scheduleDailyNotification(workManager)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Agendamento das notificações diárias
        val workManager = WorkManager.getInstance(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                scheduleDailyNotification(workManager)
                scheduleHabitReminder(workManager, 9, 0)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Para versões anteriores ao Android 13
            scheduleDailyNotification(workManager)
            scheduleHabitReminder(workManager, 9, 0)

        }

        val themePrefs = ThemePreferences(this)

        // Mock
//        val dbHelperForMock = DatabaseHelper(this)
//        val db = dbHelperForMock.writableDatabase
//        MockDataGenerator(db).insertMockData()

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
                    val newTheme =
                        if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
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
