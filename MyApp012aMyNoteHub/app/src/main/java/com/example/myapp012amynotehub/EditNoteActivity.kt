package com.example.myapp012amynotehub

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapp012amynotehub.data.Note
import com.example.myapp012amynotehub.data.NoteDao
import com.example.myapp012amynotehub.data.NoteHubDatabaseInstance
import com.example.myapp012amynotehub.databinding.ActivityEditNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDao = NoteHubDatabaseInstance.getDatabase(this).noteDao()

        // ---------- Kategorie (Spinner) ----------
        val categories = listOf("General", "Škola", "Práce", "Osobní")

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Získáme ID poznámky z Intentu
        noteId = intent.getIntExtra("note_id", -1)
        binding.tvNoteId.text = "ID: $noteId"

        binding.spinnerCategory.adapter = adapter
        // Načteme poznámku z DB
        lifecycleScope.launch {
            noteDao.getAllNotes().collect { notes ->
                val note = notes.find { it.id == noteId }
                if (note != null) {
                    binding.etEditTitle.setText(note.title)
                    binding.etEditContent.setText(note.content)
                    binding.spinnerCategory.setSelection(adapter.getPosition(note.category
                        )
                    )
                }
            }
        }

        // Kliknutí na Uložit
        binding.btnSaveChanges.setOnClickListener {
            val updatedTitle = binding.etEditTitle.text.toString()
            val updatedContent = binding.etEditContent.text.toString()

            val updatedNote = Note(
                id = noteId,
                title = updatedTitle,
                content = updatedContent
            )

            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(updatedNote)
                finish()
            }
        }

    }
}