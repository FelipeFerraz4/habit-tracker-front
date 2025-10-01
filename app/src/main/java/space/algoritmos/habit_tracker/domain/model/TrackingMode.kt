package space.algoritmos.habit_tracker.domain.model

enum class TrackingMode {
    BINARY {
        override fun isCompleted(progress: Int, goal: Int) = progress > 0
    },
    VALUE {
        override fun isCompleted(progress: Int, goal: Int) = progress >= goal
    };

    abstract fun isCompleted(progress: Int, goal: Int): Boolean
}
