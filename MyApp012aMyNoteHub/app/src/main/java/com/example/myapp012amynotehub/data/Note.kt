package com.example.myapp012amynotehub.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, //název poznámky
    val content: String //obsah poznámky

    )
