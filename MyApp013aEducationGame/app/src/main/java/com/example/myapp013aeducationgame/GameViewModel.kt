package com.example.myapp013aeducationgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapp013aeducationgame.database.AppDatabase
import com.example.myapp013aeducationgame.database.Question
import com.example.myapp013aeducationgame.database.User
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.gameDao()

    val currentUser = MutableLiveData<User?>()

    val currentQuestion = MutableLiveData<Question?>()
    val score = MutableLiveData(0)
    val lives = MutableLiveData(3)
    val isGameOver = MutableLiveData(false)
    val isVictory = MutableLiveData(false)

    private var questionList: List<Question> = emptyList()
    private var questionIndex = 0

    // Načtení uživatele
    fun loadUser(username: String) {
        viewModelScope.launch {
            var user = dao.getUserByName(username)
            if (user == null) {
                val newUser = User(username = username)
                dao.insertUser(newUser)
                user = dao.getUserByName(username)
            }
            currentUser.value = user
        }
    }

    // Start hry s obnovením postupu
    fun startGame() {
        viewModelScope.launch {
            questionList = dao.getAllQuestions()
            val user = currentUser.value

            if (user != null && user.savedQuestionIndex > 0 && user.savedQuestionIndex < questionList.size) {
                // OBNOVENÍ HRY (POKRAČUJEME)
                questionIndex = user.savedQuestionIndex
                score.value = user.savedCurrentScore
                lives.value = user.savedLives
            } else {
                // NOVÁ HRA (OD ZAČÁTKU)
                questionIndex = 0
                score.value = 0
                lives.value = 3
            }

            isGameOver.value = false
            isVictory.value = false

            if (questionList.isNotEmpty()) {
                currentQuestion.value = questionList[questionIndex]
            }
        }
    }

    fun checkAnswer(answerIndex: Int) {
        if (isGameOver.value == true || isVictory.value == true) return

        val question = currentQuestion.value ?: return

        if (answerIndex == question.correctAnswerIndex) {
            // Správně
            score.value = (score.value ?: 0) + 100
            nextQuestion() // Jdeme dál
        } else {
            // Špatně
            lives.value = (lives.value ?: 3) - 1
            if ((lives.value ?: 0) <= 0) {
                endGame() // Konec
            } else {
                // Stále žijeme, ale musíme uložit ztrátu života do DB
                saveProgress()
            }
        }
    }

    private fun nextQuestion() {
        questionIndex++
        if (questionIndex < questionList.size) {
            currentQuestion.value = questionList[questionIndex]
            // ULOŽÍME PROGRES (Hráč postoupil, uložíme to hned)
            saveProgress()
        } else {
            isVictory.value = true
            resetProgressAndSaveHighScore() // Hra dohrána, resetujeme uloženou pozici
        }
    }

    private fun endGame() {
        isGameOver.value = true
        resetProgressAndSaveHighScore() // Prohra, resetujeme uloženou pozici
    }

    // Uloží aktuální stav (kde jsem, kolik mám bodů, kolik životů)
    private fun saveProgress() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updatedUser = user.copy(
                savedQuestionIndex = questionIndex,
                savedCurrentScore = score.value ?: 0,
                savedLives = lives.value ?: 3
            )
            dao.updateUser(updatedUser)
            // Aktualizujeme i LiveData, aby UI vědělo o změně (např. při restartu appky)
            currentUser.value = updatedUser
        }
    }

    // Resetuje progres na 0, ale zachová nejvyšší skóre
    private fun resetProgressAndSaveHighScore() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val currentScore = score.value ?: 0

            // Zjistíme, jestli je to nový rekord
            val newHighScore = if (currentScore > user.highestScore) currentScore else user.highestScore

            val updatedUser = user.copy(
                highestScore = newHighScore,
                savedQuestionIndex = 0,  // Reset pozice
                savedCurrentScore = 0,   // Reset bodů běhu
                savedLives = 3           // Reset životů
            )
            dao.updateUser(updatedUser)
            currentUser.value = updatedUser
        }
    }
}