package space.algoritmos.habit_tracker.data.local.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.domain.model.DailyProgress
import java.time.LocalDate

class ProgressJsonConverterTest {

    @Test
    fun `map is correctly serialized and deserialized`() {
        val today = LocalDate.of(2025, 10, 1)
        val dailyProgression = DailyProgress(10f, 5f)
        val map = mapOf(today to dailyProgression)

        val json = ProgressJsonConverter.toJson(map)
        val result = ProgressJsonConverter.fromJson(json)

        assertEquals(dailyProgression, result[today])
    }

    @Test
    fun `empty or null json returns empty map`() {
        assertTrue(ProgressJsonConverter.fromJson(null).isEmpty())
        assertTrue(ProgressJsonConverter.fromJson("").isEmpty())
    }
}
