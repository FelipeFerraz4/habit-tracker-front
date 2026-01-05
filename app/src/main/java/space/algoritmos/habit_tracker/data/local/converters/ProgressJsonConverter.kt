package space.algoritmos.habit_tracker.data.local.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import space.algoritmos.habit_tracker.domain.model.DailyProgress
import java.time.LocalDate

object ProgressJsonConverter {

    private val gson = Gson()

    private val type = object :
        TypeToken<Map<String, DailyProgress>>() {}.type

    fun fromJson(json: String?): Map<LocalDate, DailyProgress> {
        if (json.isNullOrBlank()) return emptyMap()

        val stringMap: Map<String, DailyProgress> =
            gson.fromJson(json, type)

        return stringMap.mapKeys { LocalDate.parse(it.key) }
    }

    fun toJson(progress: Map<LocalDate, DailyProgress>): String {
        val stringMap = progress.mapKeys { it.key.toString() }
        return gson.toJson(stringMap)
    }
}
