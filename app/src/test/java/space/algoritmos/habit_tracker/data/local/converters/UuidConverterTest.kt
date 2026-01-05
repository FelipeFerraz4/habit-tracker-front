package space.algoritmos.habit_tracker.data.local.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class UuidConverterTest {

    @Test
    fun `convert UUID to string and back`() {
        val uuid = UUID.randomUUID()
        val str = UuidConverter.toString(uuid)
        val result = UuidConverter.fromString(str)

        assertEquals(uuid, result)
    }

    @Test
    fun `null values return null`() {
        assertNull(UuidConverter.toString(null))
        assertNull(UuidConverter.fromString(null))
    }
}
