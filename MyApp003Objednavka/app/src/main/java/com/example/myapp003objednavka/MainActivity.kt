package com.example.myapp003objednavka

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp003objednavka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = "Objednávka kola"

        //binding settings
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnObjednat.setOnClickListener {
            // načtení ID vybraného radioButtonu z  radioGroup
            val bikeRbId = binding.rgBikes.checkedRadioButtonId
            val bike = findViewById<RadioButton>(bikeRbId)

            // boolean
            val fork = binding.cbFork.isChecked
            val saddle = binding.cbSaddle.isChecked
            val handleBar = binding.cbHandleBar.isChecked

            val orderText = "Souhrn Objednávky:\n" +"${bike.text}\n" +
                    (if(fork) "Lepší vidlice\n" else "") +
                    (if(saddle) "Lepší sedlo\n" else "") +
                    (if(handleBar) "Lepší řidítko\n" else "")



            // přiřazení do textView hodnotu v OrderText
            binding.tvOrder.text = orderText

        }

        // Změna obrázku v závislosti na vybraném radio buttonu
        binding.rbBike1.setOnClickListener {
            binding.ivBike.setImageResource(R.drawable.bike_1)
        }
        binding.rbBike2.setOnClickListener {
            binding.ivBike.setImageResource(R.drawable.bike_2)

        }
    }
}