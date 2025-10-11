package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004moreactivities.databinding.ActivityThirdBinding
import kotlin.jvm.java

class ThirdActivity : AppCompatActivity() {

    //Vytvoření objektu binding
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using binding
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val vek = intent.getStringExtra("VEK")
        val benchpress = intent.getStringExtra("BENCHPRESS")

        // Zobrazení dat v TextView
        binding.tvInfoThird.text = """
            Získaná data:
            Přezdívka: $nickname
            Věk: $vek
            Benchpress: $benchpress
        """.trimIndent()

        //tlačítko pro zavření aktivity
        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}
