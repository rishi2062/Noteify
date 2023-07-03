package com.example.vit20bps1033ass3.model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.vit20bps1033ass3.repository.ToDoRepository

class ToDoViewModelFactory(val repository: ToDoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToDoViewModel(repository) as T
    }
}