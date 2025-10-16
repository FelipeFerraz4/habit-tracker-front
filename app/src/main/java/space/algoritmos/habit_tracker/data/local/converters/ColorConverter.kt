package space.algoritmos.habit_tracker.data.local.converters

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt

object ColorConverter {

    // Aceita: #RRGGBB, #AARRGGBB e nomes suportados por toColorInt
    // Retorna Color ou null se inválido
    fun fromString(value: String?): Color? {
        val s = value?.trim() ?: return null
        // Opcional: validar formatos hex antes para feedback mais claro
        // Regex para #RRGGBB ou #AARRGGBB
        val hexRegex = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
        return try {
            // toColorInt aceita #RRGGBB, #AARRGGBB e nomes como "red", "blue", etc.
            // Lança IllegalArgumentException se inválido
            val intColor = if (hexRegex.matches(s) || isNamedColor(s)) {
                s.toColorInt()
            } else {
                // Se não casar, ainda tentar nomes válidos; se falhar, cai no catch
                s.toColorInt()
            }
            Color(intColor)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    // Serializa sempre em #AARRGGBB
    fun toString(color: Color?): String? {
        return color?.let {
            val argb = it.toArgb() // ARGB Int em sRGB
            String.format("#%08X", argb) // AARRGGBB
        }
    }

    private fun isNamedColor(s: String): Boolean {
        // Lista baseada em parseColor/toColorInt
        return when (s.lowercase()) {
            "red","blue","green","black","white","gray","grey","lightgray","darkgray",
            "lightgrey","darkgrey","cyan","magenta","yellow","aqua","fuchsia","lime",
            "maroon","navy","olive","purple","silver","teal" -> true
            else -> false
        }
    }
}
