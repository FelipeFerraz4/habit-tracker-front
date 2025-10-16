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

private fun Habit.streakSeries(): List<com.github.mikephil.charting.data.Entry> {
    if (progress.isEmpty()) return emptyList()
    val dates = progress.keys.sorted()
    var streak = 0
    val list = mutableListOf<com.github.mikephil.charting.data.Entry>()
    for (d in dates) {
        streak = if ((progress[d] ?: 0) > 0) streak + 1 else 0
        list += com.github.mikephil.charting.data.Entry(d.toX(), streak.toFloat())
    }
    return list
}

@Composable
fun HabitStreakLineChart(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val points = remember(habit) { habit.streakSeries() }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        factory = { ctx ->
            com.github.mikephil.charting.charts.LineChart(ctx).apply {
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
            val dataSet = com.github.mikephil.charting.data.LineDataSet(points, "Streak").apply {
                color = colors.tertiary.toArgb()
                setCircleColor(colors.tertiary.toArgb())
                valueTextColor = colors.onSurfaceVariant.toArgb()
                lineWidth = 2f
                circleRadius = 0f
                setDrawValues(false)
                setDrawFilled(false)
                mode = com.github.mikephil.charting.data.LineDataSet.Mode.LINEAR
                highLightColor = colors.secondary.toArgb()
            }
            chart.data = com.github.mikephil.charting.data.LineData(dataSet)

            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_1
            ) {
                private val tv = findViewById<android.widget.TextView>(android.R.id.text1)
                private val vf = DateAxisFormatter("dd MMM")
                override fun refreshContent(e: com.github.mikephil.charting.data.Entry?, h: com.github.mikephil.charting.highlight.Highlight?) {
                    val date = vf.getFormattedValue(e?.x ?: 0f)
                    tv.text = chart.context.getString(R.string.streak_chart_tooltip_format, date, e?.y?.toInt())
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
