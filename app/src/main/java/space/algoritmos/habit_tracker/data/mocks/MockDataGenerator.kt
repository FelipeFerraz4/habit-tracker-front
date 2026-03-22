package space.algoritmos.habit_tracker.data.mocks

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate
import java.util.UUID

class MockDataGenerator(private val db: SQLiteDatabase) {

    fun insertMockData() {
        // Limpa o banco para evitar duplicatas e lixo
        db.execSQL("DELETE FROM habits")

        val habits = listOf(
            // Nome, Cor (Hex), Meta, Unidade
            Triple("Drink Water", "#2196F3", 2000f),
            Triple("Read Book", "#4CAF50", 20f),
            Triple("Exercises", "#FF9800", 30f),
            Triple("Meditation", "#9C27B0", 10f)
        )

        val today = LocalDate.now()

        for ((name, color, goal) in habits) {
            val values = ContentValues().apply {
                put("id", UUID.randomUUID().toString())
                put("name", name)
                put("color", color)
                put("status", "ACTIVE")
                put("unit", getUnitFor(name))
                put("goal", goal)
                // Gerando o progresso com a chave "done" correta
                put("progress", generateProgressJson(today, goal))
            }
            db.insert("habits", null, values)
        }
    }

    private fun getUnitFor(name: String): String = when (name) {
        "Drink Water" -> "ml"
        "Read Book" -> "pág"
        "Exercises" -> "min"
        else -> "unid"
    }

    private fun generateProgressJson(today: LocalDate, goal: Float): String {
        val entries = mutableListOf<String>()

        // Vamos gerar 45 dias de dados para o Heatmap ficar bem preenchido
        for (i in 0..45) {
            val date = today.minusDays(i.toLong())

            // Lógica para o Heatmap não ficar perfeito (mais realismo)
            // Se o resto da divisão por 5 for 0, o usuário "falhou" no dia
            if (i % 5 != 0) {
                // Alterna entre meta batida e progresso parcial
                val doneValue = if (i % 3 == 0) goal else goal * 0.6f

                // IMPORTANTE: Mudei de "value" para "done" para bater com sua classe Habit
                entries.add("\"$date\":{\"goal\":$goal,\"done\":$doneValue}")
            }
        }

        return "{${entries.joinToString(",")}}"
    }
}