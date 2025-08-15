package space.algoritmos.habit_tracker.database

import space.algoritmos.habit_tracker.model.Habit
import java.util.UUID

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = UUID.randomUUID(),
        name = name,
        color = Converters().fromColor(color),
        trackingMode = trackingMode,
        goal = goal,
        progress = Converters().fromProgressMap(progress)
    )
}

fun HabitEntity.toModel(): Habit {
    return Habit(
        id = id.hashCode(),
        name = name,
        color = Converters().toColor(color),
        trackingMode = trackingMode,
        goal = goal,
        progress = Converters().toProgressMap(progress)
    )
}
