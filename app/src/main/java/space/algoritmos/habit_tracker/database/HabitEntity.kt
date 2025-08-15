package space.algoritmos.habit_tracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import space.algoritmos.habit_tracker.model.TrackingMode
import java.util.UUID

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val color: Int, // salvamos como ARGB int
    val trackingMode: TrackingMode,
    val goal: Float,
    val progress: String // salvamos como JSON ou string serializada
)
