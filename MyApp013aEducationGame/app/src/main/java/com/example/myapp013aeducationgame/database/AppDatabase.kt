package com.example.myapp013aeducationgame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Question::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cyber_defender_db"
                )
                    // Callback pro naplnění dat při prvním spuštění
                    .addCallback(RoomCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Třída, která naplní DB otázkami při prvním vytvoření
    private class RoomCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.gameDao())
                }
            }
        }

        suspend fun populateDatabase(dao: GameDao) {
            // Tady natvrdo vložíme otázky, aby tam něco bylo
            val questions = listOf(
                Question(text = "Co znamená zkratka RAM?", answerA = "Random Access Memory", answerB = "Read Access Memory", answerC = "Run All Morning", correctAnswerIndex = 0),
                Question(text = "Který jazyk se používá pro Android?", answerA = "Swift", answerB = "Kotlin", answerC = "Pascal", correctAnswerIndex = 1),
                Question(text = "Co je to Room?", answerA = "Místnost v domě", answerB = "Databázová knihovna", answerC = "Grafický editor", correctAnswerIndex = 1),
                Question(text = "Jak se nazývá hlavní soubor aplikace?", answerA = "MainFile", answerB = "AndroidManifest", answerC = "Index.html", correctAnswerIndex = 1),
                Question(text = "Který typ proměnné je 'true'?", answerA = "Int", answerB = "String", answerC = "Boolean", correctAnswerIndex = 2)
            )
            dao.insertQuestions(questions)
        }
    }
}