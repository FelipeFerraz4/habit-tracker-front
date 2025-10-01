package space.algoritmos.habit_tracker.data.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import space.algoritmos.habit_tracker.data.local.converters.ProgressJsonConverter
import java.time.LocalDate

class ProgressJsonConverterTest {

    @Test
    fun `map is correctly serialized and deserialized`() {
        val today = LocalDate.of(2025, 10, 1)
        val map = mapOf(today to 5)

        val json = ProgressJsonConverter.toJson(map)
        val result = ProgressJsonConverter.fromJson(json)

        assertEquals(5, result[today])
    }

    @Test
    fun `empty or null json returns empty map`() {
        assertTrue(ProgressJsonConverter.fromJson(null).isEmpty())
        assertTrue(ProgressJsonConverter.fromJson("").isEmpty())
    }
}
