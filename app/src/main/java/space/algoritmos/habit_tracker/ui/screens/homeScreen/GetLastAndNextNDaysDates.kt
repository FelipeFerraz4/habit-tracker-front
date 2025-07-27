package space.algoritmos.habit_tracker.ui.screens.homeScreen

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getLastAndNextNDaysDates(pastDays: Int = 7, futureDays: Int = 3): List<String> {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<String>()

    // Começa 7 dias atrás
    calendar.add(Calendar.DAY_OF_YEAR, -pastDays + 1)

    // Total de dias = pastDays + futureDays
    val totalDays = pastDays + futureDays

    repeat(totalDays) {
        dates.add(formatter.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return dates
}
