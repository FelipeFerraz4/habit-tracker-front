package space.algoritmos.habit_tracker.ui.screens.habitRegisterScreen.utils

fun Float.toInputString(): String =
    when {
        this == 0f -> ""
        this % 1f == 0f -> this.toInt().toString()
        else -> this.toString()
    }
