package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import java.time.temporal.WeekFields
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarData

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate


private data class WeekKey(val year: Int, val week: Int)

private fun Map<LocalDate, Int>.toWeeklyBars(): List<BarEntry> {
    if (isEmpty()) return emptyList()
    val wf = WeekFields.ISO
    val grouped = entries.groupBy {
        val w = it.key.get(wf.weekOfWeekBasedYear())
        val y = it.key.get(wf.weekBasedYear())
        WeekKey(y, w)
    }.toSortedMap(compareBy<WeekKey>({ it.year }, { it.week }))

    var xIndex = 0f
    return grouped.values.map { list ->
        val sum = list.sumOf { it.value }.toFloat()
        BarEntry(xIndex++, sum)
    }
}

@Composable
fun WeeklyBarChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    selectedHabit: Habit? = null
) {
    val colors = MaterialTheme.colorScheme
    val entries = remember(habits, selectedHabit) {
        val source = selectedHabit?.progress ?: habits
            .flatMap { it.progress.entries }
            .groupBy({ it.key }) { it.value }
            .mapValues { it.value.sum() }
        if (source.isEmpty()) emptyList() else source.toWeeklyBars()
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        factory = { ctx ->
            com.github.mikephil.charting.charts.BarChart(ctx).apply {
                description.isEnabled = false
                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true
                setTouchEnabled(true)
                legend.isEnabled = false

                xAxis.apply {
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                }
                axisLeft.apply {
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    axisMinimum = 0f
                }
                axisRight.isEnabled = false

                setNoDataText("Sem dados")
                setNoDataTextColor(colors.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, "Semanas").apply {
                color = colors.primary.toArgb()
                valueTextColor = colors.onSurfaceVariant.toArgb()
                setDrawValues(false)
                highLightAlpha = 60
                highLightColor = colors.secondary.toArgb()
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.6f }

            // Tooltip simples
            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_1
            ) {
                private val tv = findViewById<android.widget.TextView>(android.R.id.text1)
                override fun refreshContent(e: com.github.mikephil.charting.data.Entry?, h: com.github.mikephil.charting.highlight.Highlight?) {
                    tv.text = chart.context.getString(R.string.weekly_chart_tooltip_format, e?.y?.toInt())
                    tv.setTextColor(colors.onSurface.toArgb())
                    tv.setBackgroundColor(colors.surfaceContainerHigh.toArgb())
                    super.refreshContent(e, h)
                }
                override fun getX(): Float = (-width / 2).toFloat()
                override fun getY(): Float = (-height).toFloat()
            }

            chart.invalidate()
        }
    )
}
