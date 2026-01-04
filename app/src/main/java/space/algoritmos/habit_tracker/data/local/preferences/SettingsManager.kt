package space.algoritmos.habit_tracker.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsManager(private val context: Context) {

    companion object {
        // Chave para a cor do heatmap
        val HEATMAP_COLOR_KEY = intPreferencesKey("heatmap_color")
    }

    // Agora usa a instância central 'appDataStore' importada
    val heatmapColorFlow: Flow<Int> = context.appDataStore.data
        .map { preferences ->
            preferences[HEATMAP_COLOR_KEY] ?: 0xFF2196F3.toInt()
        }

    suspend fun setHeatmapColor(color: Int) {
        // Usa a instância central 'appDataStore' para editar
        context.appDataStore.edit { settings ->
            settings[HEATMAP_COLOR_KEY] = color
        }
    }
}
