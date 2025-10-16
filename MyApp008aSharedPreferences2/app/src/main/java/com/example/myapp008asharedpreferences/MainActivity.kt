package com.example.myapp008asharedpreferences

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp008asharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //přístup k shared preferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.btnUlozit.setOnClickListener {
            val jmeno = binding.etJmeno.text.toString()
            val ageString = binding.etVek.text.toString().trim()

            if (ageString.isBlank()) {
                Toast.makeText(this, "Zadejte věk", Toast.LENGTH_SHORT).show()
            }
            else {
                val age = ageString.toInt()
                val isAdult = binding.checkBoxDone.isChecked
                if ((age < 18 && isAdult) || (age >= 18 && !isAdult)) {
                    Toast.makeText(this, "Co zkoušíš, mladej?", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Ukládám data...", Toast.LENGTH_SHORT).show()
                    editor.apply {
                        putString("JMENO", jmeno)
                        putInt("VEK", age)
                        putBoolean("ISADULT", isAdult)
                        apply()
                    }
                }
            }
        }
    }
}

