package com.example.myapp012amynotehub.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    //CREATE
    @Insert
    suspend fun insert(note: Note)

    //READ - načte všechny poznámky a vrátí je jako flow, které umožňuje pozorování změn
    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    //UPDATE
    @Update
    suspend fun update(note: Note)

    //DELETE
    @Delete
    suspend fun delete(note: Note)

}