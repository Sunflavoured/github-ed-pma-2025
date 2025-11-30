package com.example.myapp014asharedtasklist

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog // Import pro dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp014asharedtasklist.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        // Nastavení adapteru s novým parametrem pro Editaci
        adapter = TaskAdapter(
            tasks = emptyList(),
            onChecked = { task -> toggleCompleted(task) },
            onDelete = { task -> deleteTask(task) },
            onEdit = { task -> showEditDialog(task) } // <--- Předáváme funkci pro editaci
        )

        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        binding.buttonAdd.setOnClickListener {
            val title = binding.inputTask.text.toString()
            if (title.isNotEmpty()) {
                addTask(title)
                binding.inputTask.text.clear()
            }
        }

        listenForTasks()
    }

    private fun addTask(title: String) {
        // ID se nevyplňuje, Firestore ho vytvoří sám
        val task = Task(title = title, completed = false)
        db.collection("tasks").add(task)
    }

    // --- OPRAVENÁ FUNKCE (používá ID) ---
    private fun toggleCompleted(task: Task) {
        // Místo složitého hledání podle názvu jdeme přímo po ID dokumentu
        db.collection("tasks")
            .document(task.id) // <--- ZDE JE ZMĚNA, používáme ID
            .update("completed", !task.completed)
    }

    // --- OPRAVENÁ FUNKCE (používá ID) ---
    private fun deleteTask(task: Task) {
        db.collection("tasks")
            .document(task.id) // <--- ZDE JE ZMĚNA
            .delete()
    }

    // --- NOVÁ FUNKCE: Zobrazení dialogu pro úpravu ---
    private fun showEditDialog(task: Task) {
        val input = EditText(this)
        input.setText(task.title) // Předvyplníme stávající text

        AlertDialog.Builder(this)
            .setTitle("Upravit úkol")
            .setView(input)
            .setPositiveButton("Uložit") { _, _ ->
                val newTitle = input.text.toString()
                if (newTitle.isNotEmpty()) {
                    updateTaskTitle(task, newTitle)
                }
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }

    // --- NOVÁ FUNKCE: Odeslání změny do Firebase ---
    private fun updateTaskTitle(task: Task, newTitle: String) {
        db.collection("tasks")
            .document(task.id)
            .update("title", newTitle)
    }

    private fun listenForTasks() {
        db.collection("tasks")
            .addSnapshotListener { snapshots, _ ->
                val taskList = snapshots?.toObjects(Task::class.java) ?: emptyList()
                // Seřadíme úkoly: nehotové nahoře, hotové dole
                val sortedList = taskList.sortedBy { it.completed }
                adapter.submitList(sortedList)
            }
    }
}