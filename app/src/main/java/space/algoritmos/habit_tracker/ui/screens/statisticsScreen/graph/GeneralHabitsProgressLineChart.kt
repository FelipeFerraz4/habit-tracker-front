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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

// Metadados para o Marker (nome do hábito, data, valor bruto e meta)
private data class PointMeta(
    val habitName: String,
    val date: LocalDate,
    val raw: Int,
    val goal: Int
)

@Composable
fun GeneralHabitsProgressLineChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    daysToShow: Int = 30,
    normalizeToPercent: Boolean = true
) {
    val colors = MaterialTheme.colorScheme

    // 1) Eixo temporal comum de N dias; 2) Um dataset por hábito; 3) Normalização por meta (0–100%)
    val lineDataSets = remember(habits, daysToShow, normalizeToPercent) {
        val today = LocalDate.now()
        val startDate = today.minusDays(daysToShow.toLong() - 1)

        habits.map { habit ->
            val safeGoal = habit.goal.coerceAtLeast(1)
            val entries = (0 until daysToShow).map { offset ->
                val date = startDate.plusDays(offset.toLong())
                val raw = habit.progressOn(date)
                val y = if (normalizeToPercent) {
                    (raw.toFloat() / safeGoal) * 100f
                } else {
                    raw.toFloat()
                }
                Entry(date.toX(), y).apply {
                    data = PointMeta(habit.name, date, raw, safeGoal)
                }
            }

            LineDataSet(entries, habit.name).apply {
                color = habit.color.toArgb()
                setCircleColor(habit.color.toArgb())
                lineWidth = 5.5f
                circleRadius = 4f
                setDrawValues(false)
                setDrawFilled(false)
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

                // Legenda para identificar cada linha (um hábito)
                legend.apply {
                    isEnabled = true
                    textColor = colors.onSurface.toArgb()
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    isWordWrapEnabled = true
                }

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
                    if (normalizeToPercent) {
                        axisMaximum = 100f
                        valueFormatter = object : ValueFormatter() {
                            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                                return "${value.toInt()}%"
                            }
                        }
                    }
                }
                axisRight.isEnabled = false

                setNoDataText("Sem dados")
                setNoDataTextColor(colors.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            chart.data = LineData(lineDataSets)

            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()

            // Janela e foco no último dia
            chart.setVisibleXRangeMaximum(daysToShow.toFloat())
            val lastX = lineDataSets.firstOrNull()?.values?.lastOrNull()?.x ?: 0f
            chart.moveViewToX(lastX)

            // Marker/tooltip com hábito, data e progresso (valor bruto e % se normalizado)
            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_1
            ) {
                private val tv = findViewById<android.widget.TextView>(android.R.id.text1)
                private val vf = DateAxisFormatter("dd MMM")
                override fun refreshContent(
                    e: Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val dateTxt = vf.getFormattedValue(e?.x ?: 0f)
                    // Nome do hábito a partir do DataSet selecionado
                    val habitName = h?.dataSetIndex
                        ?.let { idx -> chart.data?.getDataSetByIndex(idx)?.label }
                        ?: ""
                    // Metadados brutos do ponto
                    val meta = e?.data as? PointMeta
                    val raw = meta?.raw ?: e?.y?.toInt() ?: 0
                    val percentTxt = if (normalizeToPercent) " (${e?.y?.toInt()}%)" else ""
                    tv.text = "$habitName\n$dateTxt\n$raw$percentTxt"
                    tv.setTextColor(colors.onSurface.toArgb())
                    tv.setBackgroundColor(colors.surfaceContainerHigh.toArgb())
                    super.refreshContent(e, h)
                }
                override fun getX(): Float = (-width / 2).toFloat()
                override fun getY(): Float = (-height).toFloat()
            }

            // Animação de entrada
            chart.animateX(700)
        }
    )
}
