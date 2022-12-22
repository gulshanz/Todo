package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoDetail(
    @PrimaryKey(autoGenerate = true) val id: Int = -1,
    val title: String,
    val description: String,
    var isCompleted: Boolean
)