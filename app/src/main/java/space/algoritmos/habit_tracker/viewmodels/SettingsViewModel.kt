package space.algoritmos.habit_tracker.viewmodels

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb // <<-- 1. ADICIONE ESTA IMPORTAÇÃO
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import space.algoritmos.habit_tracker.data.local.preferences.SettingsManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsManager = SettingsManager(application)

    private val defaultColor = Color(0xFF2196F3)
    val heatmapColor: StateFlow<Color> = settingsManager.heatmapColorFlow
        .map { colorInt -> Color(colorInt) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = defaultColor
        )

    fun setHeatmapColor(color: Color) {
        viewModelScope.launch {
            // --- 2. CORREÇÃO PRINCIPAL AQUI ---
            // Salva a cor usando toArgb() para obter um Int ARGB correto.
            settingsManager.setHeatmapColor(color.toArgb())
        }
    }
}
