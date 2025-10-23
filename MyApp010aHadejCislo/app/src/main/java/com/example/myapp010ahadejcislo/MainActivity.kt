package com.example.myapp010ahadejcislo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp010ahadejcislo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var secretNumber = 0 // tajné číslo
    private var attempts = 0 // počítání pokusů


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       newNumber()

        binding.btnCheck.setOnClickListener {
            //uložíme zadanou hodnotu do proměnné
            val guessTip = binding.etGuess.text.toString()
            //kontrola že není prázdná
            if (guessTip.isNotEmpty()) {
                attempts++
                val guess = guessTip.toInt()
                when {
                    guess != secretNumber -> {
                        binding.tvResult.text = "Špatně! Počet pokusů: $attempts"

                    }
                    else -> {
                        binding.tvResult.text = "Správně! Počet pokusů: $attempts"
                        newNumber()
                        attempts = 0
                        binding.etGuess.text.clear()
                    }
                }
            }
        }
    }
    private fun newNumber() {
        secretNumber = Random.nextInt(1, 11)
    }
}