package com.example.myapp013aeducationgame

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game) // Tady už jsi

        // TENTO KÓD SKRYJE STAVOVOU LIŠTU
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.hide(android.view.WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Načtení jména, které jsme poslali z MainActivity
        val username = intent.getStringExtra("USERNAME") ?: "Unknown"

        // Inicializace UI prvků
        val tvPlayerName = findViewById<TextView>(R.id.tvPlayerName)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvLives = findViewById<TextView>(R.id.tvLives)
        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        val btnA = findViewById<Button>(R.id.btnAnswerA)
        val btnB = findViewById<Button>(R.id.btnAnswerB)
        val btnC = findViewById<Button>(R.id.btnAnswerC)

        tvPlayerName.text = "Agent: $username"

        // Inicializace ViewModelu
        viewModel.loadUser(username)
        viewModel.startGame()

        // --- SLEDOVÁNÍ ZMĚN (OBSERVERS) ---

        // 1. Změna otázky
        viewModel.currentQuestion.observe(this, Observer { question ->
            if (question != null) {
                tvQuestion.text = question.text
                btnA.text = question.answerA
                btnB.text = question.answerB
                btnC.text = question.answerC
            }
        })

        // 2. Změna skóre
        viewModel.score.observe(this, Observer { newScore ->
            tvScore.text = "Score: $newScore"
        })

        // 3. Změna životů
        viewModel.lives.observe(this, Observer { lives ->
            tvLives.text = "FIREWALL INTEGRITY: ${lives * 33}%"
        })

        // 4. Konec hry (Prohra)
        viewModel.isGameOver.observe(this, Observer { isOver ->
            if (isOver) {
                Toast.makeText(this, "SYSTEM BREACHED! GAME OVER.", Toast.LENGTH_LONG).show()
                finish() // Ukončí aktivitu a vrátí se na login
            }
        })

        // 5. Vítězství
        viewModel.isVictory.observe(this, Observer { isWin ->
            if (isWin) {
                Toast.makeText(this, "SYSTEM SECURED! WELL DONE.", Toast.LENGTH_LONG).show()
                finish()
            }
        })

        // --- OVLÁDÁNÍ TLAČÍTEK ---
        btnA.setOnClickListener { viewModel.checkAnswer(0) }
        btnB.setOnClickListener { viewModel.checkAnswer(1) }
        btnC.setOnClickListener { viewModel.checkAnswer(2) }
    }
}