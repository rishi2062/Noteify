package com.example.vit20bps1033ass3.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class ToDo(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "Title") val title: String,
    val timeStamp: String,
    val dueDate: String, val flag: Int
)