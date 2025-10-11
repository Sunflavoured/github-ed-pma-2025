package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004moreactivities.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Declare the binding variable
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSecondAct.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val vek = binding.etVek.text.toString()
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("NICK_NAME", nickname)
                putExtra("VEK", vek)
             }
            startActivity(intent)

        }
    }
}