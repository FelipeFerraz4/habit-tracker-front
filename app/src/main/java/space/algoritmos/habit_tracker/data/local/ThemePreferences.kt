package space.algoritmos.habit_tracker.data.local

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { LIGHT, DARK }

private val Context.dataStore by preferencesDataStore(name = "settings")
private val THEME_KEY = intPreferencesKey("theme")

class ThemePreferences(private val context: Context) {

    val themeFlow: Flow<ThemeMode> = context.dataStore.data
        .map { prefs ->
            val ordinal = prefs[THEME_KEY] ?: ThemeMode.DARK.ordinal
            ThemeMode.entries[ordinal]
        }

    suspend fun saveTheme(theme: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.ordinal
        }
    }
}
