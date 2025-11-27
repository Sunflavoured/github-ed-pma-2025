package com.example.myapp013aeducationgame.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val questionId: Long = 0,
    val text: String,          // Text otázky
    val answerA: String,
    val answerB: String,
    val answerC: String,
    val correctAnswerIndex: Int // 0 pro A, 1 pro B, 2 pro C
)