package space.algoritmos.habit_tracker.domain.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HabitStatusTest {

    @Test
    fun `ACTIVE can track`() {
        Assertions.assertTrue(HabitStatus.ACTIVE.canTrack())
    }

    @Test
    fun `DELETED cannot track`() {
        Assertions.assertFalse(HabitStatus.DELETED.canTrack())
    }

}