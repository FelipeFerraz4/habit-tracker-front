package space.algoritmos.habit_tracker.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "daily_habit_database.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createHabitsTable = """
            CREATE TABLE habits (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                color TEXT NOT NULL,
                status TEXT NOT NULL,
                unit TEXT NOT NULL,
                goal REAL NOT NULL,
                progress TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createHabitsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        var version = oldVersion

        if (version < 2) {
            // Migração simples (caso venha da versão antiga)
            db.execSQL("DROP TABLE IF EXISTS habits")
            onCreate(db)
            version = 2
        }

        // Futuras migrações:
        // if (version < 3) { ... }
    }

    fun getWritable(): SQLiteDatabase = writableDatabase
    fun getReadable(): SQLiteDatabase = readableDatabase
}
