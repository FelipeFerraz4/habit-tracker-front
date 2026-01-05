package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
// Importações para i18n
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.math.min

private data class PieMeta(
    val habitName: String,
    val rawSum: Float,
    val target: Float,
    val percent: Float
)

@Composable
fun WeeklyProgressPieChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    startOfWeek: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
    capAt100: Boolean = true // limita cada hábito a no máximo 100% da meta semanal
) {
    val colorScheme = MaterialTheme.colorScheme

    val weekLabelText = stringResource(id = R.string.chart_week_label)
    val noDataText = stringResource(id = R.string.chart_no_data)

    val localizedDateFormatter = remember {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault())
    }
    val weekDates = remember(startOfWeek) { (0..6).map { startOfWeek.plusDays(it.toLong()) } }
    val weekIntervalLabel = "${weekDates.first().format(localizedDateFormatter)} – ${weekDates.last().format(localizedDateFormatter)}"
    val centerLabel = "$weekLabelText\n$weekIntervalLabel"

    val entries = remember(habits, weekDates, capAt100) {
        habits.map { habit ->
            val rawSum = weekDates.sumOf { day -> habit.progressOn(day).done.toDouble() }
            val target = (habit.goal.coerceAtLeast(1f)) * 7
            val ratio = if (target > 0) rawSum.toFloat() / target else 0f
            val capped = if (capAt100) min(ratio, 1f) else ratio
            val percent = ((ratio * 100f)).coerceAtLeast(0f)
            PieEntry(capped, habit.name).apply {
                data = PieMeta(habit.name, rawSum.toFloat(), target, percent)
            }
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                setUsePercentValues(false)
                setDrawEntryLabels(false)
                isDrawHoleEnabled = true
                holeRadius = 45f
                transparentCircleRadius = 50f

                // Cor do buraco central
                setHoleColor(colorScheme.surface.toArgb())

                // Texto central
                setCenterTextColor(colorScheme.onSurface.toArgb())
                centerText = centerLabel
                setCenterTextSize(14f)

                legend.apply {
                    isEnabled = true
                    textColor = colorScheme.onSurface.toArgb()
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    isWordWrapEnabled = true
                    form = Legend.LegendForm.CIRCLE
                    formSize = 12f
                    formToTextSpace = 8f
                    xEntrySpace = 12f
                    yEntrySpace = 6f
                }

                setNoDataText(noDataText)
                setNoDataTextColor(colorScheme.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = PieDataSet(entries, "").apply {
                colors = habits.map { it.color.toArgb() }
                sliceSpace = 2f
                selectionShift = 6f
                setDrawValues(false)
            }

            chart.data = PieData(dataSet)
            chart.centerText = centerLabel
            chart.setExtraOffsets(0f, 6f, 0f, 18f)

            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_2
            ) {
                private val title = findViewById<android.widget.TextView>(android.R.id.text1)
                private val subtitle = findViewById<android.widget.TextView>(android.R.id.text2)
                override fun refreshContent(
                    e: com.github.mikephil.charting.data.Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val meta = e?.data as? PieMeta
                    title.text = meta?.habitName ?: ""

                    subtitle.text = chart.context.getString(
                        R.string.chart_pie_tooltip_format,
                        meta?.rawSum ?: 0f,
                        meta?.target ?: 0f,
                        meta?.percent ?: 0f
                    )

                    title.setTextColor(colorScheme.onSurface.toArgb())
                    subtitle.setTextColor(colorScheme.onSurfaceVariant.toArgb())
                    setBackgroundColor(colorScheme.surfaceContainerHigh.toArgb())
                    super.refreshContent(e, h)
                }
                override fun getX(): Float = (-width / 2).toFloat()
                override fun getY(): Float = (-height).toFloat()
            }

            chart.animateY(700)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}
