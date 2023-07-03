package com.example.vit20bps1033ass3.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vit20bps1033ass3.model.ToDo

@Dao
interface ToDoDao {
    @Insert
    suspend fun insertTodo(toDo: ToDo)

    @Update
    suspend fun updateTodo(toDo: ToDo)

    @Delete
    suspend fun deleteTodo(toDo: ToDo)

    @Query("SELECT * FROM TODO where flag=0 order by dueDate asc")
    fun getAllTodo(): LiveData<List<ToDo>>

    @Query("SELECT * FROM TODO WHERE flag =1 order by dueDate")
    fun getFinishedTodo(): LiveData<List<ToDo>>
}