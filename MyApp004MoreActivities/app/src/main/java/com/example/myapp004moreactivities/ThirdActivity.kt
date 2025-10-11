package com.example.myapp004moreactivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding
import com.example.myapp004moreactivities.databinding.ActivityThirdBinding


class ThirdActivity : BaseActivity() {

    //Vytvoření objektu binding
    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate your specific layout into the BaseActivity’s content frame
        setContentLayout(R.layout.activity_third)

        // Bind it to the view inside the base layout
        binding = ActivityThirdBinding.bind(baseBinding.contentFrame.getChildAt(0))

        // Set the title in the shared top bar
        baseBinding.topAppBar.title = "Výsledky"

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
