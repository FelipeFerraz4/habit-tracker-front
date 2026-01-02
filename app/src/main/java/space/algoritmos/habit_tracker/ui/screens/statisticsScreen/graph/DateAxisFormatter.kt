package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun LocalDate.toX(): Float = this.toEpochDay().toFloat()
fun Float.toDate(): LocalDate = LocalDate.ofEpochDay(this.toLong())

/**
 * Formata um valor do eixo do gráfico (que representa um dia) para uma string de data
 * localizada, respeitando as configurações de idioma e região do dispositivo.
 *
 * @param style O estilo de formatação da data (por exemplo, SHORT, MEDIUM). Padrão é SHORT.
 */
class DateAxisFormatter(
    private val style: FormatStyle = FormatStyle.SHORT
) : ValueFormatter() {
    private fun getFormatter(): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(style)
            .withLocale(Locale.getDefault())
    }

    override fun getFormattedValue(value: Float): String {
        return value.toDate().format(getFormatter())
    }
}
