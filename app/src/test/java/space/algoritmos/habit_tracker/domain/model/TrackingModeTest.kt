package space.algoritmos.habit_tracker.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TrackingModeTest {

    @Test
    fun `enum contains expected values`() {
        val values = TrackingMode.entries.toSet()
        assertTrue(values.contains(TrackingMode.BINARY))
        assertTrue(values.contains(TrackingMode.VALUE))
        assertEquals(2, values.size)
    }

    @Test
    fun `valueOf returns correct enum`() {
        assertEquals(TrackingMode.BINARY, TrackingMode.valueOf("BINARY"))
        assertEquals(TrackingMode.VALUE, TrackingMode.valueOf("VALUE"))
    }

    @Test
    fun `ordinal matches expected order`() {
        assertEquals(0, TrackingMode.BINARY.ordinal)
        assertEquals(1, TrackingMode.VALUE.ordinal)
    }

    @Test
    fun `binary is completed when progress greater than 0`() {
        assertTrue(TrackingMode.BINARY.isCompleted(1, 10))
        assertFalse(TrackingMode.BINARY.isCompleted(0, 10))
    }

    @Test
    fun `value is completed when progress reaches goal`() {
        assertTrue(TrackingMode.VALUE.isCompleted(10, 10))
        assertFalse(TrackingMode.VALUE.isCompleted(5, 10))
    }

}
