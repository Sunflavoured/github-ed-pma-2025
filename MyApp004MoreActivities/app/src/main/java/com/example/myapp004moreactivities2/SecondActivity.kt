package com.example.myapp004moreactivities2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val tvInfo = findViewById<TextView>(R.id.tvInfo)
        // Načtení dat z předchozího  intentu
        val nickName = intent.getStringExtra("NICK_NAME")
        tvInfo.text = "Data z předchozí aktivity: $nickName"

        val btnClose = findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }

    }
}

