package space.algoritmos.habit_tracker.ui.screens.statisticsScreen.graph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
// Importações para i18n
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import space.algoritmos.habit_tracker.R
import space.algoritmos.habit_tracker.domain.model.Habit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun WeeklyStatusDonut(
    habit: Habit,
    modifier: Modifier = Modifier,
    startOfWeek: LocalDate = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
    capAt100: Boolean = true
) {
    val cs = MaterialTheme.colorScheme

    val weekLabel = stringResource(id = R.string.chart_week_label)
    val noDataText = stringResource(id = R.string.chart_no_data)
    val completedLabel = stringResource(id = R.string.donut_chart_completed)
    val remainingLabel = stringResource(id = R.string.donut_chart_remaining)

    val fmt = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault()) }
    val days = remember(startOfWeek) { (0..6).map { startOfWeek.plusDays(it.toLong()) } }
    val weekIntervalLabel = "${days.first().format(fmt)} – ${days.last().format(fmt)}"
    val centerLabel = "$weekLabel\n$weekIntervalLabel"

    val weeklyGoal = (habit.goal.coerceAtLeast(1)) * 7
    val raw = remember(habit, days) { days.sumOf { habit.progressOn(it) } }
    val done = if (capAt100) minOf(raw, weeklyGoal) else raw
    val remaining = (weeklyGoal - done).coerceAtLeast(0)

    val entries = remember(done, remaining, completedLabel, remainingLabel) {
        listOf(
            PieEntry(done.toFloat(), completedLabel),
            PieEntry(remaining.toFloat(), remainingLabel)
        )
    }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(300.dp),
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                setUsePercentValues(false)
                setDrawEntryLabels(false)
                isDrawHoleEnabled = true
                holeRadius = 45f
                transparentCircleRadius = 50f

                setHoleColor(cs.surface.toArgb())
                setTransparentCircleColor(cs.surface.toArgb())
                setTransparentCircleAlpha(0)
                setCenterTextColor(cs.onSurface.toArgb())
                setCenterTextSize(14f)
                centerText = centerLabel

                legend.apply {
                    isEnabled = true
                    textColor = cs.onSurface.toArgb()
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

                setNoDataText(noDataText)
                setNoDataTextColor(cs.onSurfaceVariant.toArgb())
            }
        },
        update = { chart ->
            val ds = PieDataSet(entries, "").apply {
                //setColors(listOf(habit.color.toArgb(), cs.outlineVariant.toArgb()))
                colors = listOf(habit.color.toArgb(), cs.outlineVariant.toArgb())
                sliceSpace = 2f
                selectionShift = 6f
                setDrawValues(false)
            }
            chart.data = PieData(ds)
            chart.centerText = centerLabel
            chart.setExtraOffsets(0f, 6f, 0f, 18f)

            chart.marker = object : com.github.mikephil.charting.components.MarkerView(
                chart.context, android.R.layout.simple_list_item_2
            ) {
                private val title = findViewById<android.widget.TextView>(android.R.id.text1)
                private val subtitle = findViewById<android.widget.TextView>(android.R.id.text2)

                override fun refreshContent(
                    e: com.github.mikephil.charting.data.Entry?,
                    h: com.github.mikephil.charting.highlight.Highlight?
                ) {
                    val pe = e as? PieEntry
                    val label = pe?.label ?: ""
                    val v = pe?.y?.toInt() ?: 0
                    val pct = if (weeklyGoal > 0) ((v.toFloat() / weeklyGoal) * 100f).toInt() else 0

                    title.text = label
                    subtitle.text = chart.context.getString(
                        R.string.donut_chart_tooltip_format,
                        v,
                        weeklyGoal,
                        pct
                    )

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
