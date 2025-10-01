package space.algoritmos.habit_tracker.data.local.converters

import java.util.UUID

object UuidConverter {
    fun fromString(value: String?): UUID? {
        return value?.let { UUID.fromString(it) }
    }

    fun toString(uuid: UUID?): String? {
        return uuid?.toString()
    }
}
