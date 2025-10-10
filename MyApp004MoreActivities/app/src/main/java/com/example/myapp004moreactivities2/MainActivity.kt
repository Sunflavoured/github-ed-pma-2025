package com.example.myapp004moreactivities2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSecondAct = findViewById<Button>(R.id.btnSecondAct)
        val etNickName = findViewById<EditText>(R.id.etNickName)

        btnSecondAct.setOnClickListener {
            val nickName = etNickName.text.toString()
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("NICK_NAME", nickName)
            startActivity(intent)
        }
    }
}