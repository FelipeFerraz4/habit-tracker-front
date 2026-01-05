package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

/**
 * Formatter padrão para eixo Y quando NÃO está em percentual.
 *
 * - Mostra inteiros quando possível
 * - Mostra 1 casa decimal quando necessário
 */
object DefaultAxisValueFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: com.github.mikephil.charting.components.AxisBase?): String {
        return if (value % 1f == 0f) {
            value.roundToInt().toString()
        } else {
            String.format("%.1f", value)
        }
    }
}
