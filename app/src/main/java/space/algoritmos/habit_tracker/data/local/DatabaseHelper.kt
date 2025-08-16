package space.algoritmos.habit_tracker.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "habit_tracker_database.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE habits (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                color TEXT,
                tracking_mode TEXT,
                goal REAL,
                progress TEXT
            )
        """.trimIndent()
        try {
            db?.execSQL(createTable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL("DROP TABLE IF EXISTS habits")
            onCreate(db)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Função utilitária para abrir banco
    fun getDbWritable(): SQLiteDatabase = writableDatabase
    fun getDbReadable(): SQLiteDatabase = readableDatabase
}
