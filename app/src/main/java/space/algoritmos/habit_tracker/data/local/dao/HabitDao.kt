package space.algoritmos.habit_tracker.data.local.dao

import android.content.ContentValues
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import space.algoritmos.habit_tracker.data.local.DatabaseHelper
import space.algoritmos.habit_tracker.domain.model.Habit
import space.algoritmos.habit_tracker.domain.model.TrackingMode
import java.time.LocalDate
import java.util.UUID

class HabitDao(private val dbHelper: DatabaseHelper) {

    private val gson = Gson()

    fun addHabit(habit: Habit) {
        val db = dbHelper.writableDatabase
        try {
            Log.d("HabitDao", "Tentando salvar hábito: $habit")
            val values = ContentValues().apply {
                put("id", habit.id.toString())
                put("name", habit.name)
                val colorHex = String.format("#%06X", 0xFFFFFF and habit.color.toArgb())
                put("color", colorHex)
                put("tracking_mode", habit.trackingMode.name)
                put("goal", habit.goal)
                put("progress", gson.toJson(habit.progress.mapKeys { it.key.toString() }))
            }
            db.insert("habits", null, values)
            Log.d("HabitDao", "Hábito salvo com sucesso: $habit")
        } catch (e: Exception) {
            Log.e("HabitDao", "Exceção ao adicionar hábito", e)
        } finally {
            db.close()
        }
    }

    fun getAllHabits(): List<Habit> {
        val db = dbHelper.getDbReadable()
        val habits = mutableListOf<Habit>()
        try {
            Log.d("HabitDao", "Buscando todos os hábitos")
            val cursor = db.query("habits", null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    val id = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("id")))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val color = Color(cursor.getString(cursor.getColumnIndexOrThrow("color")).toColorInt())
                    val trackingMode = TrackingMode.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("tracking_mode")))
                    val goal = cursor.getInt(cursor.getColumnIndexOrThrow("goal"))
                    val progressJson = cursor.getString(cursor.getColumnIndexOrThrow("progress")) ?: "{}"
                    val type = object : TypeToken<Map<String, Int>>() {}.type
                    val progressStringMap: Map<String, Int> = gson.fromJson(progressJson, type)
                    val progressMap = progressStringMap.mapKeys { LocalDate.parse(it.key) }

                    habits.add(Habit(id, name, color, trackingMode, goal, progressMap))
                } while (cursor.moveToNext())
            }
            Log.d("HabitDao", "Hábitos carregados: ${habits.size}")
            cursor.close()
        } catch (e: Exception) {
            Log.e("HabitDao", "Erro ao buscar hábitos", e)
        } finally {
            db.close()
        }
        return habits
    }

    fun getHabitById(id: UUID): Habit? {
        val db = dbHelper.getDbReadable()
        var habit: Habit? = null
        try {
            Log.d("HabitDao", "Buscando hábito pelo ID: $id")
            val cursor = db.query(
                "habits",
                null,
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )

            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val color = Color(cursor.getString(cursor.getColumnIndexOrThrow("color")).toColorInt())
                val trackingMode = TrackingMode.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("tracking_mode")))
                val goal = cursor.getInt(cursor.getColumnIndexOrThrow("goal"))
                val progressJson = cursor.getString(cursor.getColumnIndexOrThrow("progress")) ?: "{}"
                val type = object : TypeToken<Map<String, Int>>() {}.type
                val progressStringMap: Map<String, Int> = gson.fromJson(progressJson, type)
                val progressMap = progressStringMap.mapKeys { LocalDate.parse(it.key) }

                habit = Habit(id, name, color, trackingMode, goal, progressMap)
                Log.d("HabitDao", "Hábito encontrado: $habit")
            } else {
                Log.d("HabitDao", "Hábito não encontrado para ID: $id")
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("HabitDao", "Erro ao buscar hábito pelo ID", e)
        } finally {
            db.close()
        }
        return habit
    }

    fun updateHabit(habit: Habit) {
        val db = dbHelper.getDbWritable()
        try {
            Log.d("HabitDao", "Atualizando hábito: $habit")
            val values = ContentValues().apply {
                put("name", habit.name)
                val colorHex = String.format("#%06X", 0xFFFFFF and habit.color.toArgb())
                put("color", colorHex)
                put("tracking_mode", habit.trackingMode.name)
                put("goal", habit.goal)
                put("progress", gson.toJson(habit.progress.mapKeys { it.key.toString() }))
            }
            db.update("habits", values, "id = ?", arrayOf(habit.id.toString()))
            Log.d("HabitDao", "Hábito atualizado com sucesso: $habit")
        } catch (e: Exception) {
            Log.e("HabitDao", "Erro ao atualizar hábito", e)
        } finally {
            db.close()
        }
    }

    fun deleteHabit(id: UUID) {
        val db = dbHelper.getDbWritable()
        try {
            Log.d("HabitDao", "Deletando hábito com ID: $id")
            db.delete("habits", "id = ?", arrayOf(id.toString()))
            Log.d("HabitDao", "Hábito deletado com sucesso: $id")
        } catch (e: Exception) {
            Log.e("HabitDao", "Erro ao deletar hábito", e)
        } finally {
            db.close()
        }
    }
}
