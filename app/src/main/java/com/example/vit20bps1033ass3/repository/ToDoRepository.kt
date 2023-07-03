package com.example.vit20bps1033ass3.repository

import androidx.lifecycle.LiveData
import com.example.vit20bps1033ass3.Dao.ToDoDao
import com.example.vit20bps1033ass3.model.ToDo


class ToDoRepository(val toDoDao: ToDoDao) {

    fun getAllTodo(): LiveData<List<ToDo>> = toDoDao.getAllTodo()
    fun getFinishedTodo(): LiveData<List<ToDo>> = toDoDao.getFinishedTodo()
    suspend fun insertTodo(toDo: ToDo) = toDoDao.insertTodo(toDo)
    suspend fun deleteTodo(toDo: ToDo) = toDoDao.deleteTodo(toDo)
    suspend fun updateTodo(toDo: ToDo) = toDoDao.updateTodo(toDo)
    suspend fun updateFinishedTodo(toDo: ToDo) = toDoDao.updateTodo(toDo)
}