package com.example.myapp017vanocniaplikace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp017vanocniaplikace.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataStore: ChristmasDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStore = ChristmasDataStore(requireContext())

        // --- AKCE TLAČÍTKA ULOŽIT ---
        binding.btnSave.setOnClickListener {
            val budgetString = binding.inputBudget.text.toString()

            if (budgetString.isNotEmpty()) {
                val budgetValue = budgetString.toInt()

                lifecycleScope.launch {
                    dataStore.saveBudget(budgetValue)
                    Toast.makeText(context, "Rozpočet $budgetValue Kč uložen!", Toast.LENGTH_SHORT).show()
                    binding.inputBudget.text.clear()
                }
            } else {
                Toast.makeText(context, "Zadej prosím částku", Toast.LENGTH_SHORT).show()
            }
        }

        // --- AKCE TLAČÍTKA RESET (Vynuluje pouze limit) ---
        binding.btnReset.setOnClickListener {
            lifecycleScope.launch {
                // Zavoláme resetBudget, aby se dárky nesmazaly
                dataStore.resetBudget()
                Toast.makeText(context, "Limit rozpočtu byl vynulován na 0 Kč.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}