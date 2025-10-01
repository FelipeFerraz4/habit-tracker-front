package space.algoritmos.habit_tracker.data.local.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

object ProgressJsonConverter {
    private val gson = Gson()
    private val type = object : TypeToken<Map<String, Int>>() {}.type

    fun fromJson(json: String?): Map<LocalDate, Int> {
        if (json.isNullOrEmpty()) return emptyMap()
        val stringMap: Map<String, Int> = gson.fromJson(json, type)
        return stringMap.mapKeys { LocalDate.parse(it.key) }
    }

    fun toJson(progress: Map<LocalDate, Int>): String {
        val stringMap = progress.mapKeys { it.key.toString() }
        return gson.toJson(stringMap)
    }
}
