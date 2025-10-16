package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import java.time.LocalDate
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.format.DateTimeFormatter

fun LocalDate.toX(): Float = this.toEpochDay().toFloat()
fun Float.toDate(): LocalDate = LocalDate.ofEpochDay(this.toLong())

class DateAxisFormatter(
    pattern: String = "dd/MM"
) : ValueFormatter() {
    private val fmt = DateTimeFormatter.ofPattern(pattern)
    override fun getFormattedValue(value: Float): String {
        return value.toDate().format(fmt)
    }
}
