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
import kotlin.jvm.java

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_third)

        val tvInfoThird = findViewById<TextView>(R.id.tvInfoThird)

        //Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val vek = intent.getStringExtra("VEK")
        val benchpress = intent.getStringExtra("BENCHPRESS")

        // Show it in TextView
        tvInfoThird.text =
            "Získaná data:\nPřezdívka: $nickname\nVěk: $vek\nBenchpress: $benchpress"

        val btnClose = findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }
    }



    }
