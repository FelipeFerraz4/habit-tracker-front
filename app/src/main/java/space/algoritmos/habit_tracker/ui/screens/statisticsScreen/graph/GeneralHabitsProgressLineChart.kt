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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.ui.screens.statisticsScreen.utils.DefaultAxisValueFormatter
import java.time.LocalDate
import java.time.format.FormatStyle

// Metadata usado pelo Marker
private data class PointMeta(
    val habitName: String,
    val date: LocalDate,
    val raw: Float,
    val goal: Float
)

@Composable
fun GeneralHabitsProgressLineChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    daysToShow: Int = 30,
    normalizeToPercent: Boolean = true
) {
    val colors = MaterialTheme.colorScheme
    val noDataText = stringResource(id = R.string.chart_no_data)

    val lineDataSets = remember(habits, daysToShow, normalizeToPercent) {
        val today = LocalDate.now()
        val startDate = today.minusDays(daysToShow.toLong() - 1)

        habits.map { habit ->
            val entries = (0 until daysToShow).map { offset ->
                val date = startDate.plusDays(offset.toLong())

                val daily = habit.progressOn(date)
                val safeGoal = daily.goal.coerceAtLeast(1f)
                val raw = daily.done

                val y = if (normalizeToPercent) {
                    (raw / safeGoal) * 100f
                } else {
                    raw
                }

                Entry(date.toX(), y).apply {
                    data = PointMeta(
                        habitName = habit.name,
                        date = date,
                        raw = raw,
                        goal = safeGoal
                    )
                }
            }

            LineDataSet(entries, habit.name).apply {
                color = habit.color.toArgb()
                setCircleColor(habit.color.toArgb())
                lineWidth = 2.5f
                circleRadius = 4f
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = habit.color.toArgb()
                fillAlpha = 40
                mode = LineDataSet.Mode.CUBIC_BEZIER
                highLightColor = colors.secondary.toArgb()
            }
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        factory = { ctx ->
            LineChart(ctx).apply {
                description.isEnabled = false
                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true
                setTouchEnabled(true)

                legend.apply {
                    isEnabled = true
                    textColor = colors.onSurface.toArgb()
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    isWordWrapEnabled = true
                }

                xAxis.apply {
                    valueFormatter = DateAxisFormatter()
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    granularity = 1f
                    isGranularityEnabled = true
                    textSize = 12f
                }

                axisLeft.apply {
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    axisMinimum = 0f
                    textSize = 12f
                }

                axisRight.isEnabled = false

                setNoDataText(noDataText)
                setNoDataTextColor(colors.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            chart.data = LineData(lineDataSets)

            // 🔹 Eixo Y dinâmico (NÃO limita a 100%)
            val dataMax = chart.data?.yMax ?: 0f
            chart.axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = dataMax * 1.15f

                valueFormatter = if (normalizeToPercent) {
                    object : ValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase): String {
                            return "${value.toInt()}%"
                        }
                    }
                } else DefaultAxisValueFormatter
            }

            chart.setVisibleXRangeMaximum(daysToShow.toFloat())
            val lastX = lineDataSets.firstOrNull()?.values?.lastOrNull()?.x ?: 0f
            chart.moveViewToX(lastX)

            // 🔹 Marker / Tooltip
            chart.marker = object :
                com.github.mikephil.charting.components.MarkerView(
                    chart.context,
                    android.R.layout.simple_list_item_1
                ) {

                private val tv =
                    findViewById<android.widget.TextView>(android.R.id.text1)
                private val vf = DateAxisFormatter(FormatStyle.MEDIUM)

                override fun refreshContent(
                    e: Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val meta = e?.data as? PointMeta
                    val dateTxt = vf.getFormattedValue(e?.x ?: 0f)
                    val habitName = meta?.habitName ?: ""
                    val raw = meta?.raw ?: 0f
                    val percent = e?.y ?: 0f

                    tv.text = if (normalizeToPercent) {
                        chart.context.getString(
                            R.string.chart_progress_tooltip_percent,
                            habitName,
                            dateTxt,
                            raw,
                            percent
                        )
                    } else {
                        chart.context.getString(
                            R.string.chart_progress_tooltip_raw,
                            habitName,
                            dateTxt,
                            raw
                        )
                    }

                    tv.setTextColor(colors.onSurface.toArgb())
                    tv.setBackgroundColor(colors.surfaceContainerHigh.toArgb())
                    super.refreshContent(e, h)
                }

                override fun getX(): Float = (-width / 2f)
                override fun getY(): Float = (-height.toFloat())
            }

            chart.animateX(700)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}
