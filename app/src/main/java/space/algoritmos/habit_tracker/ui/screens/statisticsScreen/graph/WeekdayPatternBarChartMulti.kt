package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

private data class DayMeta(val samples: Int)

@Composable
fun WeekdayPatternBarChartMulti(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    weeksBack: Int = 8,
    capAt100: Boolean = true
) {
    val cs = MaterialTheme.colorScheme
    val labels = listOf("Seg","Ter","Qua","Qui","Sex","Sáb","Dom")

    val entries = remember(habits, weeksBack, capAt100) {
        val today = LocalDate.now()
        val start = today.minusDays((7L * weeksBack) - 1L)
        val sums = DoubleArray(7)
        val counts = IntArray(7)
        var d = start
        while (!d.isAfter(today)) {
            val idx = when (d.dayOfWeek) {
                java.time.DayOfWeek.MONDAY -> 0
                java.time.DayOfWeek.TUESDAY -> 1
                java.time.DayOfWeek.WEDNESDAY -> 2
                java.time.DayOfWeek.THURSDAY -> 3
                java.time.DayOfWeek.FRIDAY -> 4
                java.time.DayOfWeek.SATURDAY -> 5
                java.time.DayOfWeek.SUNDAY -> 6
            }
            // Considera apenas pontos registrados por hábito no dia (inclui zeros registrados)
            habits.forEach { habit ->
                if (habit.progress.containsKey(d)) {
                    val goal = habit.goal.coerceAtLeast(1)
                    val raw = habit.progressOn(d).toDouble()
                    val ratio = raw / goal
                    val capped = if (capAt100) ratio.coerceAtMost(1.0) else ratio
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

                setNoDataText("Sem dados")
                setNoDataTextColor(cs.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, "Padrão por dia (normalizado)").apply {
                setColor(cs.primary.toArgb())
                setDrawValues(false)
                highLightColor = cs.secondary.toArgb()
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.6f }
            chart.setFitBars(true)

            // Marker: dia + média % + amostras (quantos pontos compõem a média)
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

                    title.text = "$day — $pct%"
                    subtitle.text = "Amostras: $samples"

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
