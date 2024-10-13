package ru.sergeevdmitry8i11.catsandmice

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "game_results.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "GameStats"
        const val TOTAL_CLICKS = "totalClicks"
        const val MOUSE_CLICKS = "mouseClicks"
        const val GAME_TIME = "gameTime"
        const val ACCURACY = "accuracy"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $TOTAL_CLICKS INTEGER,
                $MOUSE_CLICKS INTEGER,
                $GAME_TIME TEXT,
                $ACCURACY REAL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertGameResult(totalClicks: Int, mouseClicks: Int, gameTime: String): Long{
        val accuracy = (mouseClicks.toFloat() / totalClicks) * 100
        val values = ContentValues().apply {
            put(TOTAL_CLICKS, totalClicks)
            put(MOUSE_CLICKS, mouseClicks)
            put(GAME_TIME, gameTime)
            put(ACCURACY, accuracy)
        }
        val db = writableDatabase
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getLastTenGames(): List<GameStats>{
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null, null, null, null, null, "id DESC", "10"
        )
        val games = mutableListOf<GameStats>()
        if (cursor.moveToFirst()){
            do {
                val game = GameStats(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    totalClicks = cursor.getInt(cursor.getColumnIndexOrThrow(TOTAL_CLICKS)),
                    mouseClicks = cursor.getInt(cursor.getColumnIndexOrThrow(MOUSE_CLICKS)),
                    gameTime = cursor.getString(cursor.getColumnIndexOrThrow(GAME_TIME)),
                    percentage = cursor.getFloat(cursor.getColumnIndexOrThrow(ACCURACY))
                )
                games.add(game)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return games
    }
}