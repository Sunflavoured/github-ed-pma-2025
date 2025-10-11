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
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding
import kotlin.jvm.java

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using binding
        val binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val vek = intent.getStringExtra("VEK")
        binding.twInfo.text = "Data z první aktivity. Přezdívka: $nickname. Váš věk: $vek"


        binding.btnThirdAct.setOnClickListener {
            val benchpress = binding.etBenchpress.text.toString()
            val intent = Intent(this, ThirdActivity::class.java )
            intent.putExtra("BENCHPRESS", benchpress)
            intent.putExtra("NICK_NAME", nickname)
            intent.putExtra("VEK", vek)
            startActivity(intent)
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

    }
}