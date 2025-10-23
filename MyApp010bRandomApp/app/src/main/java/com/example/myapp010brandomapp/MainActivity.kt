package com.example.myapp010brandomapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp010brandomapp.databinding.ActivityMainBinding
import kotlin.random.Random
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //inicializace skore
    private var userScore = 0
    private var computerScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nastavení listenerů pro všechna tlačítka
        binding.btnKamen.setOnClickListener {
            playRound("kamen")
        }

        binding.btnNuzky.setOnClickListener {
            playRound("nuzky")
        }

        binding.btnPapir.setOnClickListener {
            playRound("papir")
        }

        // Nová hra
        binding.btnNovaHra.setOnClickListener {
            userScore = 0
            computerScore = 0
            binding.tvUserScore.text = "Hráč: 0"
            binding.tvComputerScore.text = "Počítač: 0"
            binding.tvResult.text = "Nová hra! Vyber možnost."
        }
    }

    //funkce pro hru
    private fun playRound(userChoice: String) {
        val options = listOf("kamen", "nuzky", "papir")
        binding.tvResult.text = "Počítač přemýšlí..." // 🧠 efekt myšlení

        // Simulace "přemýšlení" počítače
        Handler(Looper.getMainLooper()).postDelayed({
            val computerChoice = options[Random.nextInt(options.size)]
            val resultText: String

            if (userChoice == computerChoice) {
                resultText = "Remíza! Oba jste zvolili $userChoice."
            } else if (
                (userChoice == "kamen" && computerChoice == "nuzky") ||
                (userChoice == "nuzky" && computerChoice == "papir") ||
                (userChoice == "papir" && computerChoice == "kamen")
            ) {
                userScore++
                resultText = "Vyhráváš! Počítač zvolil $computerChoice."
            } else {
                computerScore++
                resultText = "Prohráváš! Počítač zvolil $computerChoice."
            }

            // Aktualizace UI po zpoždění
            binding.tvResult.text = resultText
            binding.tvUserScore.text = "Hráč: $userScore"
            binding.tvComputerScore.text = "Počítač: $computerScore"
        }, 1000) // 1000 ms = 1 sekunda myšlení
    }
}