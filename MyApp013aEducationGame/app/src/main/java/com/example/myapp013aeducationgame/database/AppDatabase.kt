package com.example.myapp013aeducationgame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ZMĚNA: Verze 2
@Database(entities = [User::class, Question::class], version = 2, exportSchema = false)
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
                    .addCallback(RoomCallback())
                    .fallbackToDestructiveMigration() // DŮLEŽITÉ: Povolí smazání staré DB při změně verze
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

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
            // VELKÝ SEZNAM OTÁZEK
            val questions = listOf(
                Question(text = "Co znamená zkratka RAM?", answerA = "Random Access Memory", answerB = "Read Access Memory", answerC = "Run All Morning", correctAnswerIndex = 0),
                Question(text = "Který jazyk je nativní pro Android?", answerA = "Swift", answerB = "Kotlin", answerC = "Pascal", correctAnswerIndex = 1),
                Question(text = "Co je to 'Activity' v Androidu?", answerA = "Tělocvik", answerB = "Jedna obrazovka", answerC = "Databáze", correctAnswerIndex = 1),
                Question(text = "Jaký soubor definuje vzhled?", answerA = "Java", answerB = "XML", answerC = "Gradle", correctAnswerIndex = 1),
                Question(text = "Co dělá 'finish()'?", answerA = "Ukončí aktivitu", answerB = "Smaže aplikaci", answerC = "Formátuje disk", correctAnswerIndex = 0),

                Question(text = "Co je to 'Bug'?", answerA = "Brouk v PC", answerB = "Chyba v kódu", answerC = "Antivirus", correctAnswerIndex = 1),
                Question(text = "Co znamená HTML?", answerA = "HyperText Markup Language", answerB = "High Tech Machine Learning", answerC = "Home Tool Maker List", correctAnswerIndex = 0),
                Question(text = "Která firma vyvíjí Android?", answerA = "Apple", answerB = "Microsoft", answerC = "Google", correctAnswerIndex = 2),
                Question(text = "Jak se píše komentář v Kotlinu?", answerA = "// Text", answerB = "# Text", answerC = "<!-- Text", correctAnswerIndex = 0),
                Question(text = "Co je 'Boolean'?", answerA = "Text", answerB = "Pravda/Nepravda", answerC = "Celé číslo", correctAnswerIndex = 1),

                Question(text = "Co je GitHub?", answerA = "Sociální síť", answerB = "Úložiště kódu", answerC = "Hudební přehrávač", correctAnswerIndex = 1),
                Question(text = "Který port používá HTTP?", answerA = "80", answerB = "21", answerC = "443", correctAnswerIndex = 0),
                Question(text = "Co je 'Loop'?", answerA = "Lupa", answerB = "Smyčka/Cyklus", answerC = "Kabel", correctAnswerIndex = 1),
                Question(text = "Co znamená GUI?", answerA = "Graphical User Interface", answerB = "Global Unit Index", answerC = "Game User Info", correctAnswerIndex = 0),
                Question(text = "Který OS je open-source?", answerA = "Windows", answerB = "iOS", answerC = "Linux", correctAnswerIndex = 2)
            )
            dao.insertQuestions(questions)
        }
    }
}