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

    // --- DATA PRO UI ---
    val currentUser = MutableLiveData<User?>()

    // Herní data
    val currentQuestion = MutableLiveData<Question?>()
    val score = MutableLiveData(0)
    val lives = MutableLiveData(3) // Hráč má 3 životy (firewally)
    val isGameOver = MutableLiveData(false)
    val isVictory = MutableLiveData(false)

    // Interní proměnné
    private var questionList: List<Question> = emptyList()
    private var questionIndex = 0

    // 1. Načtení uživatele podle jména (voláme z MainActivity i GameActivity)
    fun loadUser(username: String) {
        viewModelScope.launch {
            var user = dao.getUserByName(username)
            if (user == null) {
                // Pokud user neexistuje, vytvoříme ho (registrace)
                val newUser = User(username = username, highestScore = 0)
                dao.insertUser(newUser)
                user = dao.getUserByName(username)
            }
            currentUser.value = user
        }
    }

    // 2. Start hry - načtení otázek
    fun startGame() {
        viewModelScope.launch {
            questionList = dao.getAllQuestions()
            questionIndex = 0
            score.value = 0
            lives.value = 3
            isGameOver.value = false
            isVictory.value = false

            if (questionList.isNotEmpty()) {
                currentQuestion.value = questionList[questionIndex]
            }
        }
    }

    // 3. Kontrola odpovědi
    fun checkAnswer(answerIndex: Int) {
        // Pokud je konec hry, nic neděláme
        if (isGameOver.value == true || isVictory.value == true) return

        val question = currentQuestion.value ?: return

        if (answerIndex == question.correctAnswerIndex) {
            // SPRÁVNĚ -> Zvyš skóre a jdi dál
            score.value = (score.value ?: 0) + 100
            nextQuestion()
        } else {
            // ŠPATNĚ -> Uber život
            lives.value = (lives.value ?: 3) - 1
            if ((lives.value ?: 0) <= 0) {
                endGame()
            }
        }
    }

    private fun nextQuestion() {
        questionIndex++
        if (questionIndex < questionList.size) {
            currentQuestion.value = questionList[questionIndex]
        } else {
            // Došly otázky -> VÍTĚZSTVÍ
            isVictory.value = true
            saveScore()
        }
    }

    private fun endGame() {
        isGameOver.value = true
        saveScore()
    }

    // Uložení nejlepšího skóre do DB
    private fun saveScore() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val currentScore = score.value ?: 0

            if (currentScore > user.highestScore) {
                // Update v DB (Copy vytvoří kopii objektu s novou hodnotou)
                val updatedUser = user.copy(highestScore = currentScore)
                dao.updateUser(updatedUser)
            }
        }
    }
}