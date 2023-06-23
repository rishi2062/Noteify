package  com.example.vit20bps1033ass3.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.vit20bps1033ass3.repository.NoteRepository

class NoteViewModelFactory(val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(repository) as T
    }
}