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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

@Composable
fun HabitDailyProgressChart(
    habit: Habit,
    modifier: Modifier = Modifier,
    daysToShow: Int = 30
) {
    val colors = MaterialTheme.colorScheme

    val entries = remember(habit, daysToShow) {
        val today = LocalDate.now()
        val startDate = today.minusDays(daysToShow.toLong() - 1)

        (0 until daysToShow).map { offset ->
            val date = startDate.plusDays(offset.toLong())
            val value = habit.progress[date] ?: 0
            BarEntry(date.toX(), value.toFloat())
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                setScaleEnabled(true)
                setPinchZoom(true)
                isDragEnabled = true
                setTouchEnabled(true)
                legend.isEnabled = false

                xAxis.apply {
                    valueFormatter = DateAxisFormatter("dd/MM")
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    granularity = 1f
                    isGranularityEnabled = true
                    textSize = 14f
                }
                axisLeft.apply {
                    textColor = colors.onSurface.toArgb()
                    gridColor = colors.outlineVariant.toArgb()
                    axisMinimum = 0f
                    textSize = 14f
                }
                axisRight.isEnabled = false

                setNoDataText("Sem dados")
                setNoDataTextColor(colors.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = BarDataSet(entries, habit.name).apply {
                setColor(colors.primary.toArgb())
                valueTextColor = colors.onSurfaceVariant.toArgb()
                setDrawValues(false)
                highLightColor = colors.secondary.toArgb()
            }

            chart.data = BarData(dataSet).apply {
                barWidth = 0.6f
            }

            // Ajuste para encaixar exatamente as barras nas bordas (útil quando não há janela rolável completa)
            chart.setFitBars(true)

            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()

            // Janela e foco no mais recente
            chart.setVisibleXRangeMaximum(daysToShow.toFloat())
            chart.moveViewToX(entries.lastOrNull()?.x ?: 0f)

            // Tooltip (Marker)
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
                    val value = e?.y?.toInt() ?: 0
                    tv.text = chart.context.getString(
                        R.string.chart_tooltip_format,
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

            // Animação de entrada (vertical) após configurar dados/viewport
            chart.animateY(700)
        }
    )
}
