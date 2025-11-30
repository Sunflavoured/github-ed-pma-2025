package com.example.myapp013aeducationgame.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val username: String,
    val highestScore: Int = 0,

    // NOVÉ POLOŽKY PRO ULOŽENÍ STAVU HRY:
    val savedQuestionIndex: Int = 0, // Kde skončil (0 = začátek)
    val savedCurrentScore: Int = 0,  // Kolik měl bodů v rozehrané hře
    val savedLives: Int = 3          // Kolik měl životů
)