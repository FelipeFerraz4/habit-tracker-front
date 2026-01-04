package space.algoritmos.habit_tracker.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore// ÚNICA DEFINIÇÃO DO DATASTORE PARA TODO O APLICATIVO
// Qualquer arquivo que precisar do DataStore importará esta propriedade.
val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
