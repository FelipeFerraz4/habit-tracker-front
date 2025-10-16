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
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit

@Composable
fun DailyAggregateLineChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val aggregated = remember(habits) {
        habits
            .flatMap { it.progress.entries }
            .groupBy({ it.key }) { it.value }
            .mapValues { it.value.sum() }
            .toSortedMap()
    }
    val points = aggregated.map { (date, value) ->
        com.github.mikephil.charting.data.Entry(date.toX(), value.toFloat())
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        factory = { context ->
            com.github.mikephil.charting.charts.LineChart(context).apply {
                description.isEnabled = false
                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true
                setTouchEnabled(true)
                legend.isEnabled = false

                xAxis.apply {
                    valueFormatter = DateAxisFormatter("dd/MM")
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    setDrawGridLines(true)
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
            val dataSet = com.github.mikephil.charting.data.LineDataSet(points, "Total diário").apply {
                color = colors.primary.toArgb()
                setCircleColor(colors.primary.toArgb())
                valueTextColor = colors.onSurfaceVariant.toArgb()
                lineWidth = 2f
                circleRadius = 0f
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = colors.primary.copy(alpha = 0.15f).toArgb()
                mode = com.github.mikephil.charting.data.LineDataSet.Mode.LINEAR
                highLightColor = colors.secondary.toArgb()
            }

            chart.data = com.github.mikephil.charting.data.LineData(dataSet)
            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_1
            ) {
                private val tv = findViewById<android.widget.TextView>(android.R.id.text1)
                private val vf = DateAxisFormatter("dd MMM")
                override fun refreshContent(
                    e: com.github.mikephil.charting.data.Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val date = vf.getFormattedValue(e?.x ?: 0f)
                    tv.text = chart.context.getString(R.string.chart_tooltip_format, date, e?.y?.toInt())
                    tv.setTextColor(colors.onSurface.toArgb())
                    tv.setBackgroundColor(colors.surfaceContainerHigh.toArgb())
                    super.refreshContent(e, h)
                }
                override fun getX(): Float = (-width / 2f)
                override fun getY(): Float = (-height.toFloat())
            }
            chart.invalidate()
        }
    )
}
