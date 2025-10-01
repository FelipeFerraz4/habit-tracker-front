package space.algoritmos.habit_tracker.data.local.dao

import android.content.ContentValues
import android.database.Cursor
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.data.local.converters.ColorConverter
import space.algoritmos.habit_tracker.data.local.converters.ProgressJsonConverter
import space.algoritmos.habit_tracker.data.local.converters.UuidConverter
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.HabitStatus
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import java.util.UUID

class HabitDao(private val dbHelper: DatabaseHelper) {

    fun addHabit(habit: Habit) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id", UuidConverter.toString(habit.id))
            put("name", habit.name)
            put("color", ColorConverter.toString(habit.color))
            put("tracking_mode", habit.trackingMode.name)
            put("status", habit.status.name)
            put("goal", habit.goal)
            put("progress", ProgressJsonConverter.toJson(habit.progress))
        }
        db.insert("habits", null, values)
        db.close()
    }

    fun getAllHabits(): List<Habit> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("habits", null, null, null, null, null, null)
        val habits = mutableListOf<Habit>()
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
            "habits",
            null,
            "id = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        val habit = cursor.use { if (it.moveToFirst()) cursorToHabit(it) else null }
        db.close()
        return habit
    }

    fun updateHabit(habit: Habit) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", habit.name)
            put("color", ColorConverter.toString(habit.color))
            put("tracking_mode", habit.trackingMode.name)
            put("status", habit.status.name)
            put("goal", habit.goal)
            put("progress", ProgressJsonConverter.toJson(habit.progress))
        }
        db.update("habits", values, "id = ?", arrayOf(habit.id.toString()))
        db.close()
    }

    fun deleteHabit(id: UUID) {
        val db = dbHelper.writableDatabase
        db.delete("habits", "id = ?", arrayOf(id.toString()))
        db.close()
    }

    // Helper interno
    private fun cursorToHabit(cursor: Cursor): Habit {
        return Habit(
            id = UuidConverter.fromString(cursor.getString(cursor.getColumnIndexOrThrow("id")))!!,
            name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
            color = ColorConverter.fromString(cursor.getString(cursor.getColumnIndexOrThrow("color")))!!,
            trackingMode = TrackingMode.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("tracking_mode"))),
            status = HabitStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))),
            goal = cursor.getInt(cursor.getColumnIndexOrThrow("goal")),
            progress = ProgressJsonConverter.fromJson(cursor.getString(cursor.getColumnIndexOrThrow("progress")))
        )
    }
}
