package com.example.myapp014asharedtasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp014asharedtasklist.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<Task>,
    private val onChecked: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
    private val onEdit: (Task) -> Unit // <--- NOVÝ CALLBACK PRO EDITACI
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.textTitle.text = task.title

        // --- OPRAVA BUGU S CHECKBOXEM ---
        // 1. Nejdřív zrušíme listener, aby se nespustil při nastavování hodnoty
        holder.binding.checkCompleted.setOnCheckedChangeListener(null)

        // 2. Nastavíme správnou hodnotu
        holder.binding.checkCompleted.isChecked = task.completed

        // 3. Znovu nastavíme listener (reaguje jen na kliknutí uživatele)
        holder.binding.checkCompleted.setOnCheckedChangeListener { _, isChecked ->
            // Checkbox vrací jen boolean, ale my chceme volat funkci toggle
            // Voláme onChecked jen pokud se stav liší od uloženého (pojistka)
            if (isChecked != task.completed) {
                onChecked(task)
            }
        }
        // -------------------------------

        holder.binding.imageDelete.setOnClickListener {
            onDelete(task)
        }

        // --- NOVÁ FUNKCE: EDITACE ---
        // Dlouhé podržení na položce spustí editaci
        holder.itemView.setOnLongClickListener {
            onEdit(task)
            true // true znamená, že jsme událost zpracovali
        }
    }

    override fun getItemCount() = tasks.size

    fun submitList(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}