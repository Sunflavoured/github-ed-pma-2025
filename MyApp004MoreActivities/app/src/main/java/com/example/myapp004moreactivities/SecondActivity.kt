package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import com.example.myapp004moreactivities.databinding.ActivityMainBinding
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding

class SecondActivity : BaseActivity() {
    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate your specific layout into the BaseActivity’s content frame
        setContentLayout(R.layout.activity_second)

        // Bind it to the view inside the base layout
        binding = ActivitySecondBinding.bind(baseBinding.contentFrame.getChildAt(0))

        // Set the title in the shared top bar
        baseBinding.topAppBar.title = "Zadání váhy cviku"

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