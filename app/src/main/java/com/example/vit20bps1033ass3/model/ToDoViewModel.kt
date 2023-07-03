package com.example.vit20bps1033ass3.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vit20bps1033ass3.repository.ToDoRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToDoViewModel(val repository: ToDoRepository) : ViewModel() {
    val getToDo: LiveData<List<ToDo>> = repository.getAllTodo()
    val getFinishedToDo: LiveData<List<ToDo>> = repository.getFinishedTodo()

    @OptIn(DelicateCoroutinesApi::class)
    fun insertTodo(toDo: ToDo) = GlobalScope.launch {
        repository.insertTodo(toDo)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteTodo(toDo: ToDo) = GlobalScope.launch {
        repository.deleteTodo(toDo)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateTodo(toDo: ToDo) = GlobalScope.launch {
        repository.updateTodo(toDo)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateFinishedTodo(toDo: ToDo) = GlobalScope.launch {
        repository.updateFinishedTodo(toDo)
    }
}