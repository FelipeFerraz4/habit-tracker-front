package space.algoritmos.habit_tracker.domain.model

enum class HabitStatus {
    ACTIVE {
        override fun canTrack() = true
    },
    DELETED {
        override fun canTrack() = false
    };

    abstract fun canTrack(): Boolean
}
