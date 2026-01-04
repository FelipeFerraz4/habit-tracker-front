package space.algoritmos.habit_tracker.data.local.preferences // Ou .preferences se você mover

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
// Importando a instância central

enum class ThemeMode { LIGHT, DARK }

// REMOVIDA A DECLARAÇÃO LOCAL DO DATASTORE
private val THEME_KEY = intPreferencesKey("theme")

class ThemePreferences(private val context: Context) {

    // Agora usa a instância central 'appDataStore'
    val themeFlow: Flow<ThemeMode> = context.appDataStore.data
        .map { prefs ->
            val ordinal = prefs[THEME_KEY] ?: ThemeMode.DARK.ordinal
            ThemeMode.entries.getOrNull(ordinal) ?: ThemeMode.DARK
        }

    suspend fun saveTheme(theme: ThemeMode) {
        // Usa a instância central 'appDataStore' para editar
        context.appDataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.ordinal
        }
    }
}
