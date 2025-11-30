package com.example.myapp013aeducationgame.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameDao {

    // --- UŽIVATELÉ ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :name LIMIT 1")
    suspend fun getUserByName(name: String): User?

    @Query("SELECT * FROM users ORDER BY highestScore DESC")
    suspend fun getAllUsers(): List<User>

    @Update
    suspend fun updateUser(user: User)

    // --- OTÁZKY ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int
}