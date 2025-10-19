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
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.LocalDate

@Composable
fun WeeklyStatusDonut(
    habit: Habit,
    modifier: Modifier = Modifier,
    startOfWeek: LocalDate = LocalDate.now()
        .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)),
    capAt100: Boolean = true
) {
    val cs = MaterialTheme.colorScheme
    val fmt = remember { java.time.format.DateTimeFormatter.ofPattern("dd/MM") }
    val days = remember(startOfWeek) { (0..6).map { startOfWeek.plusDays(it.toLong()) } }
    val weekLabel = "${days.first().format(fmt)} – ${days.last().format(fmt)}"

    val weeklyGoal = (habit.goal.coerceAtLeast(1)) * 7
    val raw = remember(habit, days) { days.sumOf { habit.progressOn(it) } }
    val done = if (capAt100) minOf(raw, weeklyGoal) else raw
    val remaining = (weeklyGoal - done).coerceAtLeast(0)

    val entries = remember(done, remaining) {
        listOf(
            com.github.mikephil.charting.data.PieEntry(done.toFloat(), "Concluído"),
            com.github.mikephil.charting.data.PieEntry(remaining.toFloat(), "Restante")
        )
    }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(300.dp),
        factory = { ctx ->
            com.github.mikephil.charting.charts.PieChart(ctx).apply {
                description.isEnabled = false
                setUsePercentValues(false)
                setDrawEntryLabels(false)
                isDrawHoleEnabled = true
                holeRadius = 45f
                transparentCircleRadius = 50f

                // Centro adaptado ao tema
                setHoleColor(cs.surface.toArgb())
                setTransparentCircleColor(cs.surface.toArgb())
                setTransparentCircleAlpha(0)
                setCenterTextColor(cs.onSurface.toArgb())
                setCenterTextSize(14f)
                centerText = "Semana\n$weekLabel"

                legend.apply {
                    isEnabled = true
                    textColor = cs.onSurface.toArgb()
                    horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
                    orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
                    isWordWrapEnabled = true
                    form = com.github.mikephil.charting.components.Legend.LegendForm.CIRCLE
                    formSize = 12f
                    formToTextSpace = 8f
                    xEntrySpace = 12f
                    yEntrySpace = 6f
                }

                setNoDataText("Sem dados")
                setNoDataTextColor(cs.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val ds = com.github.mikephil.charting.data.PieDataSet(entries, "").apply {
                setColors(listOf(habit.color.toArgb(), cs.outlineVariant.toArgb()))
                sliceSpace = 2f
                selectionShift = 6f
                setDrawValues(false)
            }
            chart.data = com.github.mikephil.charting.data.PieData(ds)
            chart.centerText = "Semana\n$weekLabel"
            chart.setExtraOffsets(0f, 6f, 0f, 18f)

            // Marker/tooltip com rótulo, valores e percentual
            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_2
            ) {
                private val title = findViewById<android.widget.TextView>(android.R.id.text1)
                private val subtitle = findViewById<android.widget.TextView>(android.R.id.text2)

                override fun refreshContent(
                    e: com.github.mikephil.charting.data.Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val pe = e as? com.github.mikephil.charting.data.PieEntry
                    val label = pe?.label ?: ""
                    val v = pe?.y?.toInt() ?: 0
                    val pct = if (weeklyGoal > 0) ((v.toFloat() / weeklyGoal) * 100f).toInt() else 0

                    title.text = "$label"
                    subtitle.text = when (label) {
                        "Concluído" -> "$v / $weeklyGoal (${pct}%)"
                        "Restante"  -> "$v / $weeklyGoal (${pct}%)"
                        else        -> "$v"
                    }

                    title.setTextColor(cs.onSurface.toArgb())
                    subtitle.setTextColor(cs.onSurfaceVariant.toArgb())
                    setBackgroundColor(cs.surfaceContainerHigh.toArgb())
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
