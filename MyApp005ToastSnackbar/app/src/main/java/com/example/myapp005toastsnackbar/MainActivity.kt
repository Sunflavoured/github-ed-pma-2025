package com.example.myapp005toastsnackbar

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp005toastsnackbar.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShowToast.setOnClickListener {
            val toast = Toast.makeText(this, "Tohle je toast", Toast.LENGTH_SHORT)
            toast.show()
        }
        binding.btnShowSnackbar.setOnClickListener {
            val snackbar = Snackbar.make(it, "Tohle je snackbar", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.parseColor("#000000"))
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setDuration(7000)
            .setAction("Zavřt") {
                Toast.makeText(this, "Tohle je akce", Toast.LENGTH_SHORT).show()
            }
            snackbar.show()

        }
    }
}