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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.theme.Purple40
import space.algoritmos.habit_tracker.ui.theme.Purple80
import java.time.LocalDate
import java.time.format.FormatStyle

@Composable
fun DailyHabitCountBarChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    daysToShow: Int = 30
) {
    val colors = MaterialTheme.colorScheme

    val noDataText = stringResource(id = R.string.chart_no_data)
    val habitsPerDayLabel = stringResource(id = R.string.chart_habits_per_day_label)

    val entries = remember(habits, daysToShow) {
        val today = LocalDate.now()
        val startDate = today.minusDays(daysToShow.toLong() - 1)

        (0 until daysToShow).map { offset ->
            val date = startDate.plusDays(offset.toLong())
            val count = habits.count { (it.progress[date]?.done ?: 0f) > 0f }
            BarEntry(date.toX(), count.toFloat())
        }
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
                    valueFormatter = DateAxisFormatter()
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    granularity = 1f
                    isGranularityEnabled = true
                    setLabelCount(4, true)
                    textSize = 14f
                }
                axisLeft.apply {
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    axisMinimum = 0f
                    textSize = 14f
                }
                axisRight.isEnabled = false

                setNoDataText(noDataText)
                setNoDataTextColor(colors.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, habitsPerDayLabel).apply {
                setColor(Purple40.toArgb())
                valueTextColor = colors.onSurfaceVariant.toArgb()
                setDrawValues(false)
                highLightColor = Purple80.toArgb() // cor ao clicar
            }

            chart.data = BarData(dataSet).apply { barWidth = 0.6f }
            chart.setFitBars(true)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.setVisibleXRangeMaximum(daysToShow.toFloat())
            chart.moveViewToX(entries.lastOrNull()?.x ?: 0f)

            // Tooltip (já estava usando recurso de string, o que é ótimo)
            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_1
            ) {
                private val tv = findViewById<android.widget.TextView>(android.R.id.text1)
                private val vf = DateAxisFormatter(FormatStyle.MEDIUM)
                override fun refreshContent(
                    e: com.github.mikephil.charting.data.Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val date = vf.getFormattedValue(e?.x ?: 0f)
                    val value = e?.y?.toInt() ?: 0
                    tv.text = chart.context.getString(
                        R.string.daily_habit_count_format,
                        date,
                        value
                    )
                    tv.setTextColor(colors.onSurface.toArgb())
                    tv.setBackgroundColor(colors.surfaceContainerHigh.toArgb())
                    super.refreshContent(e, h)
                }
                override fun getX(): Float = (-width / 2).toFloat()
                override fun getY(): Float = (-height).toFloat()
            }
            chart.animateY(700)
        }
    )
}
