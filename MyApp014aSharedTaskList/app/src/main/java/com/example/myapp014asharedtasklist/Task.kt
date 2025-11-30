package com.example.myapp014asharedtasklist

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val id: String = "", // <--- přidání id
    val title: String = "",
    val completed: Boolean = false
)