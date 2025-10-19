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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.min

private data class PieMeta(
    val habitName: String,
    val rawSum: Int,
    val target: Int,
    val percent: Int
)

@Composable
fun WeeklyProgressPieChart(
    habits: List<Habit>,
    modifier: Modifier = Modifier,
    startOfWeek: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
    capAt100: Boolean = true // limita cada hábito a no máximo 100% da meta semanal
) {
    val colorScheme = MaterialTheme.colorScheme
    val fmt = DateTimeFormatter.ofPattern("dd/MM")
    val weekDates = remember(startOfWeek) { (0..6).map { startOfWeek.plusDays(it.toLong()) } }
    val weekLabel = "${weekDates.first().format(fmt)} – ${weekDates.last().format(fmt)}"

    val entries = remember(habits, weekDates, capAt100) {
        habits.map { habit ->
            val rawSum = weekDates.sumOf { day -> habit.progressOn(day) }
            val target = (habit.goal.coerceAtLeast(1)) * 7
            val ratio = if (target > 0) rawSum.toFloat() / target.toFloat() else 0f
            val capped = if (capAt100) min(ratio, 1f) else ratio
            val percent = ((ratio * 100f).toInt()).coerceAtLeast(0)
            PieEntry(capped, habit.name).apply {
                data = PieMeta(habit.name, rawSum, target, percent)
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
                setUsePercentValues(false) // mostramos percentuais no marker, não nos rótulos do gráfico
                setDrawEntryLabels(false)  // evita rótulos sobre as fatias
                isDrawHoleEnabled = true
                centerText = "Semana\n$weekLabel"
                setCenterTextColor(colorScheme.onSurface.toArgb())

                // ...config padrão...
                isDrawHoleEnabled = true
                holeRadius = 45f
                transparentCircleRadius = 50f

                // Centro preto sólido
                setHoleColor(colorScheme.onPrimary.toArgb())
                setTransparentCircleColor(colorScheme.onPrimary.toArgb())
                setTransparentCircleAlpha(0)

                // Texto central branco
                setCenterTextColor(colorScheme.onBackground.toArgb())
                centerText = "Semana\n$weekLabel"
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

                setNoDataText("Sem dados")
                setNoDataTextColor(colorScheme.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val dataSet = PieDataSet(entries, "").apply {
                // Cores vindas dos hábitos (uma por fatia)
                colors = habits.map { it.color.toArgb() }
                sliceSpace = 2f
                selectionShift = 6f
                setDrawValues(false) // sem valores sobre as fatias (usamos marker)
            }

            chart.data = PieData(dataSet)
            chart.centerText = "Semana\n$weekLabel"

            // Espaço extra para afastar o pie da legenda
            chart.setExtraOffsets(0f, 6f, 0f, 18f)

            // Marker/tooltip com nome, bruto e percentual normalizado da semana
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
                    val name = meta?.habitName ?: ""
                    val raw = meta?.rawSum ?: 0
                    val target = meta?.target ?: 0
                    val pct = meta?.percent ?: 0
                    title.text = name
                    subtitle.text = "$raw / $target (${pct}%)"
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
