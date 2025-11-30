package com.example.myapp013aeducationgame

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    // Automatické vytvoření ViewModelu
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Kliknutí na tlačítko
        btnLogin.setOnClickListener {
            val name = etUsername.text.toString().trim()
            if (name.isNotEmpty()) {
                // Zavoláme logiku ve ViewModelu
                viewModel.loadUser(name)
            } else {
                Toast.makeText(this, "Enter a valid codename!", Toast.LENGTH_SHORT).show()
            }
        }

        // Sledování změn
        viewModel.currentUser.observe(this, Observer { user ->
            if (user != null) {
                Toast.makeText(this, "Access Granted: ${user.username}", Toast.LENGTH_SHORT).show()

                // SPUSTÍME HRU
                val intent = android.content.Intent(this, GameActivity::class.java)
                intent.putExtra("USERNAME", user.username)
                startActivity(intent)
            }
        })
    }
}