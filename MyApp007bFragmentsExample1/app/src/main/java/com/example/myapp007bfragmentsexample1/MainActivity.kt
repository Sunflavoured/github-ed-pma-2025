package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFragment.setOnClickListener {
            replaceFragment(Fragment1())
        }

        binding.btnFragment2.setOnClickListener {
            replaceFragment(Fragment2())
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
