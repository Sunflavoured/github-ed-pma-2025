package com.example.myapp017vanocniaplikace

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp017vanocniaplikace.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: ChristmasDataStore
    private lateinit var giftAdapter: GiftAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = ChristmasDataStore(requireContext())

        // Nastavení RecyclerView
        giftAdapter = GiftAdapter(
            gifts = emptyList(),
            onCheckedChange = { updatedGift ->
                lifecycleScope.launch { dataStore.updateGift(updatedGift) }
            },
            onLongClick = { giftToDelete ->
                // Zobrazíme jednoduchý dialog pro potvrzení smazání
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Smazat dárek?")
                    .setMessage("Opravdu chcete smazat dárek '${giftToDelete.giftDescription}'?")
                    .setPositiveButton("Smazat") { _, _ ->
                        lifecycleScope.launch {
                            dataStore.deleteGift(giftToDelete.id)
                            Toast.makeText(context, "Dárek smazán", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Zrušit", null)
                    .show()
            }
        )

        binding.rvGifts.apply {
            adapter = giftAdapter
            layoutManager = LinearLayoutManager(requireContext())
            // Důležité: RecyclerView uvnitř ScrollView se občas "pere" o místo.
            // Tento řádek zajistí, že se seznam správně vykreslí:
            isNestedScrollingEnabled = false
        }

        // Sledování dat z DataStore
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataStore.budgetFlow.combine(dataStore.giftsFlow) { budget, gifts ->
                    Pair(budget, gifts)
                }.collect { (budget, gifts) ->
                    updateUI(budget, gifts)
                }
            }
        }

        // Kliknutí na tlačítko - Přidat dárek
        binding.btnAddGift.setOnClickListener {
            val person = binding.etPersonName.text.toString()
            val description = binding.etGiftDescription.text.toString()
            val priceStr = binding.etGiftPrice.text.toString()

            if (person.isNotEmpty() && description.isNotEmpty() && priceStr.isNotEmpty()) {
                val newGift = Gift(
                    personName = person,
                    giftDescription = description,
                    price = priceStr.toInt()
                )
                saveNewGift(newGift)
            } else {
                Toast.makeText(context, "Vyplň prosím všechna pole!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNewGift(gift: Gift) {
        lifecycleScope.launch {
            val currentGifts = dataStore.giftsFlow.first().toMutableList()
            currentGifts.add(gift)
            dataStore.saveGifts(currentGifts)

            // Vyčistit pole
            binding.etPersonName.text.clear()
            binding.etGiftDescription.text.clear()
            binding.etGiftPrice.text.clear()
        }
    }

    private fun updateUI(budget: Int, gifts: List<Gift>) {
        // Sčítáme pouze ZAŠKRTNUTÉ dárky
        val totalSpent = gifts.filter { it.isSelected }.sumOf { it.price }
        val remaining = budget - totalSpent

        binding.tvBudgetInfo.text = "Celkový limit: $budget Kč"
        binding.tvSpentInfo.text = "Utraceno (zaškrtnuto): $totalSpent Kč"

        // Počet dárků celkem vs. koupených
        val boughtCount = gifts.count { it.isSelected }
        // Můžeš přidat textview pro tento údaj: "Dárků: $boughtCount z ${gifts.size}"

        if (remaining < 0) {
            binding.tvRemaining.text = "V mínusu: ${Math.abs(remaining)} Kč 😱"
            binding.tvRemaining.setTextColor(Color.RED)
        } else {
            binding.tvRemaining.text = "Zbývá: $remaining Kč"
            binding.tvRemaining.setTextColor(Color.parseColor("#388E3C"))
        }

        giftAdapter.updateData(gifts)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}