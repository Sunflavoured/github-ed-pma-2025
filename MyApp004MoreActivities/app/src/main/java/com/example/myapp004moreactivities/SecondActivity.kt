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

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)

        val twInfo = findViewById<TextView>(R.id.twInfo)

        //Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val vek = intent.getStringExtra("VEK")
        twInfo.text = "Data z první aktivity. Přezdívka: $nickname. Váš věk: $vek"

        //Odeslání dat do třetí aktivity
        val btnThirdAct = findViewById<Button>(R.id.btnThirdAct)
        val etBenchpress = findViewById<EditText>(R.id.etBenchpress)

        btnThirdAct.setOnClickListener {
            val benchpress = etBenchpress.text.toString()
            val intent = Intent(this, ThirdActivity::class.java )
            intent.putExtra("BENCHPRESS", benchpress)
            intent.putExtra("NICK_NAME", nickname)
            intent.putExtra("VEK", vek)
            startActivity(intent)
        }

        val btnClose = findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }

    }
}