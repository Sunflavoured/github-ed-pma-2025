package com.example.myapp017vanocniaplikace // <-- Tvůj balíček

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapp017vanocniaplikace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 1. Nastavení navigace - Získáme NavHostFragment
        // Tady musíme stále použít ID (R.id.nav_host_fragment), abychom našli ten Fragment
        // uvnitř manažera. Binding nám dá jen View, ale my potřebujeme instanci Fragmentu.
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        // 2. Propojení menu s navigací
        // Tady už nepoužíváme findViewById, ale přistupujeme přímo přes binding
        // ID v XML bylo: android:id="@+id/bottomNavigationView" -> v kódu: binding.bottomNavigationView
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}