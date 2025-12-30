package com.example.myapp017vanocniaplikace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp017vanocniaplikace.databinding.ItemGiftBinding

class GiftAdapter(
    private var gifts: List<Gift>,
    private val onCheckedChange: (Gift) -> Unit,
    private val onLongClick: (Gift) -> Unit // Přidáno pro mazání
) : RecyclerView.Adapter<GiftAdapter.GiftViewHolder>() {
    class GiftViewHolder(val binding: ItemGiftBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        val gift = gifts[position]

        holder.binding.tvItemPerson.text = gift.personName
        holder.binding.tvItemDescription.text = gift.giftDescription
        holder.binding.tvItemPrice.text = "${gift.price} Kč"

        holder.binding.cbBought.setOnCheckedChangeListener(null)
        holder.binding.cbBought.isChecked = gift.isSelected

        holder.binding.cbBought.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(gift.copy(isSelected = isChecked))
        }

        // Detekce dlouhého podržení na celé kartě dárku
        holder.itemView.setOnLongClickListener {
            onLongClick(gift)
            true // true znamená, že jsme událost zpracovali
        }
    }

    override fun getItemCount() = gifts.size

    fun updateData(newGifts: List<Gift>) {
        gifts = newGifts
        notifyDataSetChanged()
    }
}