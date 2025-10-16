package com.example.myapp07_fragmantapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp07_fragmantapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLasagne.setOnClickListener {
            replaceFragment(FragmentLasagne())
        }
        binding.btnSalat.setOnClickListener {
            replaceFragment(FragmentSalat())
        }
        binding.btnVejce.setOnClickListener {
            replaceFragment(FragmentVejce())
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        // získání instance správce Fragmentu
        val fragmentManager = supportFragmentManager
        // vytvoření transakce
        val fragmentTransaction = fragmentManager.beginTransaction()
        //nahrazení fragmentu ve view conteineru fragmentem novým, který byl předán jako argument

        fragmentTransaction.replace(R.id.fragmentContainer, fragment)

        //potvrzení transakce a  provedení výměny fragmentu
        fragmentTransaction.commit()
    }
}
