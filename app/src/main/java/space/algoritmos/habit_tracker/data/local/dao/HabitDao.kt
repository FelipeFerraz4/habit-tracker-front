package space.algoritmos.habit_tracker.data.local.dao

import android.content.ContentValues
import android.database.Cursor
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.data.local.converters.ColorConverter
import space.algoritmos.habit_tracker.data.local.converters.ProgressJsonConverter
import space.algoritmos.habit_tracker.data.local.converters.UuidConverter
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import java.util.UUID

class HabitDao(private val dbHelper: DatabaseHelper) {

    companion object {
        private const val TABLE = "habits"

        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_COLOR = "color"
        private const val COL_STATUS = "status"
        private const val COL_UNIT = "unit"
        private const val COL_GOAL = "goal"
        private const val COL_PROGRESS = "progress"
    }

    fun addHabit(habit: Habit) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COL_ID, UuidConverter.toString(habit.id))
            put(COL_NAME, habit.name)
            put(COL_COLOR, ColorConverter.toString(habit.color))
            put(COL_STATUS, habit.status.name)
            put(COL_UNIT, habit.unit)
            put(COL_GOAL, habit.goal)
            put(COL_PROGRESS, ProgressJsonConverter.toJson(habit.progress))
        }
        db.insert(TABLE, null, values)
        db.close()
    }

    fun getAllHabits(): List<Habit> {
        val db = dbHelper.readableDatabase
        val habits = mutableListOf<Habit>()

        val cursor = db.query(TABLE, null, null, null, null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                habits.add(cursorToHabit(it))
            }
        }

        db.close()
        return habits
    }

    fun getHabitById(id: UUID): Habit? {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            TABLE,
            null,
            "$COL_ID = ?",
            arrayOf(UuidConverter.toString(id)),
            null,
            null,
            null
        )

        val habit = cursor.use {
            if (it.moveToFirst()) cursorToHabit(it) else null
        }

        db.close()
        return habit
    }

    fun updateHabit(habit: Habit) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, habit.name)
            put(COL_COLOR, ColorConverter.toString(habit.color))
            put(COL_STATUS, habit.status.name)
            put(COL_UNIT, habit.unit)
            put(COL_GOAL, habit.goal)
            put(COL_PROGRESS, ProgressJsonConverter.toJson(habit.progress))
        }

        db.update(
            TABLE,
            values,
            "$COL_ID = ?",
            arrayOf(UuidConverter.toString(habit.id))
        )

        db.close()
    }

    fun deleteHabit(id: UUID) {
        val db = dbHelper.writableDatabase
        db.delete(TABLE, "$COL_ID = ?", arrayOf(UuidConverter.toString(id)))
        db.close()
    }

    // =========================
    // Cursor → Domain
    // =========================
    private fun cursorToHabit(cursor: Cursor): Habit {
        return Habit(
            id = UuidConverter.fromString(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ID))
            )!!,
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
            color = ColorConverter.fromString(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_COLOR))
            )!!,
            status = HabitStatus.valueOf(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS))
            ),
            unit = cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)),
            goal = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_GOAL)),
            progress = ProgressJsonConverter.fromJson(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PROGRESS))
            )
        )
    }
}
