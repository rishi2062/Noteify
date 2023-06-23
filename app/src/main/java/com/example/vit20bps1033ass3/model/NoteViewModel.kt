package  com.example.vit20bps1033ass3.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vit20bps1033ass3.repository.NoteRepository
import kotlinx.coroutines.DelicateCoroutinesApi

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteViewModel(val repository: NoteRepository) : ViewModel() {
    val getNotes : LiveData<List<Notes>> = repository.getAllNotes()
    @OptIn(DelicateCoroutinesApi::class)
    fun insertNote(note: Notes) = GlobalScope.launch {
        repository.insertNote(note)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun deleteNote(note:Notes) = GlobalScope.launch {
        repository.deleteNote(note)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun updataNote(note:Notes) = GlobalScope.launch {
        repository.updateNote(note)
    }

}