package space.algoritmos.habit_tracker.database

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import space.algoritmos.habit_tracker.model.TrackingMode
import java.time.LocalDate
import java.util.UUID

class Converters {

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(value: String): UUID {
        return UUID.fromString(value)
    }

    // Color <-> Int
    @TypeConverter
    fun fromColor(color: Color): Int {
        return color.value.toInt()
    }

    @TypeConverter
    fun toColor(value: Int): Color {
        return Color(value)
    }

    // TrackingMode <-> String
    @TypeConverter
    fun fromTrackingMode(mode: TrackingMode): String {
        return mode.name
    }

    @TypeConverter
    fun toTrackingMode(value: String): TrackingMode {
        return TrackingMode.valueOf(value)
    }

    // Map<LocalDate, Float> <-> String
    @TypeConverter
    fun fromProgressMap(map: Map<LocalDate, Float>): String {
        return map.entries.joinToString(";") { "${it.key},${it.value}" }
    }

    @TypeConverter
    fun toProgressMap(data: String): Map<LocalDate, Float> {
        if (data.isBlank()) return emptyMap()
        return data.split(";").associate {
            val (dateStr, valueStr) = it.split(",")
            LocalDate.parse(dateStr) to valueStr.toFloat()
        }
    }
}
