package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import com.example.myapp004moreactivities.databinding.ActivityMainBinding
//Main activity extends BaseActivity
class MainActivity : BaseActivity() {
    // Declare the binding variable
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate your specific layout into the BaseActivity’s content frame
        setContentLayout(R.layout.activity_main)

        // Set title in the ActionBar
        supportActionBar?.title = "Main Activity"

        // Optionally enable "back" button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Bind it to the view inside the base layout
        binding = ActivityMainBinding.bind(baseBinding.contentFrame.getChildAt(0))

        // Set the title in the shared top bar
        baseBinding.topAppBar.title = "Zadání údajů"

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