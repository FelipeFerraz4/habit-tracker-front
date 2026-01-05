package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private data class DayMeta(val samples: Int)

@Composable
fun WeekdayPatternBarChartMulti(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    weeksBack: Int = 8,
    capAt100: Boolean = true
) {
    val cs = MaterialTheme.colorScheme

    val labels = remember {
        DayOfWeek.entries.map {
            it.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }
    }

    val noDataText = stringResource(id = R.string.chart_no_data)
    val chartLabel = stringResource(id = R.string.chart_weekday_pattern_label)

    val entries = remember(habits, weeksBack, capAt100) {
        val today = LocalDate.now()
        val start = today.minusDays((7L * weeksBack) - 1L)
        val sums = DoubleArray(7)
        val counts = IntArray(7)
        var d = start
        while (!d.isAfter(today)) {
            val idx = d.dayOfWeek.value - 1

            habits.forEach { habit ->
                if (habit.progress.containsKey(d)) {
                    val goal = habit.progressOn(d).goal.coerceAtLeast(1f)
                    val raw = habit.progressOn(d).done
                    val ratio = raw / goal
                    val capped = if (capAt100) ratio.coerceAtMost(1.0f) else ratio
                    sums[idx] += capped
                    counts[idx] += 1
                }
            }
            d = d.plusDays(1)
        }
        (0..6).map { i ->
            val avg = if (counts[i] > 0) (sums[i] / counts[i]) else 0.0
            val pct = (avg * 100.0).toFloat().coerceAtMost(100f)
            BarEntry(i.toFloat(), pct).apply { data = DayMeta(samples = counts[i]) }
        }
    }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(300.dp),
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = false
                setTouchEnabled(true)
                legend.isEnabled = false
                isHighlightPerTapEnabled = true

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = cs.onSurface.toArgb()
                    gridColor = cs.outlineVariant.toArgb()
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    granularity = 1f
                    isGranularityEnabled = true
                    valueFormatter = object : ValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                            val i = value.toInt()
                            return labels.getOrNull(i) ?: ""
                        }
                    }
                    textSize = 14f
                }
                axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = 100f
                    textColor = cs.onSurface.toArgb()
                    gridColor = cs.outlineVariant.toArgb()
                    setLabelCount(6, true)
                    valueFormatter = object : ValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                            return "${value.toInt()}%"
                        }
                    }
                    textSize = 14f
                }
                axisRight.isEnabled = false

                setNoDataText(noDataText)
                setNoDataTextColor(cs.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, chartLabel).apply {
                setColor(cs.primary.toArgb())
                setDrawValues(false)
                highLightColor = cs.secondary.toArgb()
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.6f }
            chart.setFitBars(true)

            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_2
            ) {
                private val title = findViewById<android.widget.TextView>(android.R.id.text1)
                private val subtitle = findViewById<android.widget.TextView>(android.R.id.text2)

                override fun refreshContent(
                    e: com.github.mikephil.charting.data.Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val idx = e?.x?.toInt() ?: 0
                    val day = labels.getOrNull(idx) ?: ""
                    val pct = e?.y?.toInt() ?: 0
                    val meta = e?.data as? DayMeta
                    val samples = meta?.samples ?: 0

                    title.text = chart.context.getString(
                        R.string.chart_weekday_pattern_tooltip_title,
                        day,
                        pct
                    )
                    subtitle.text = chart.context.getString(
                        R.string.chart_weekday_pattern_tooltip_subtitle,
                        samples
                    )

                    title.setTextColor(cs.onSurface.toArgb())
                    subtitle.setTextColor(cs.onSurfaceVariant.toArgb())
                    setBackgroundColor(cs.surfaceContainerHigh.toArgb())

                    super.refreshContent(e, h)
                }
                override fun getX(): Float = (-width / 2f)
                override fun getY(): Float = (-height.toFloat())
            }

            chart.animateY(700)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}
