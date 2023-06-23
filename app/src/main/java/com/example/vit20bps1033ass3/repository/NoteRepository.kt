package  com.example.vit20bps1033ass3.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vit20bps1033ass3.Dao.NoteDao
import com.example.vit20bps1033ass3.model.Notes


class NoteRepository(val noteDao : NoteDao) {

    fun getAllNotes() : LiveData<List<Notes>> = noteDao.getAllNotes()
    suspend fun insertNote(note : Notes) = noteDao.insertNote(note)
    suspend fun deleteNote(note : Notes) = noteDao.deleteNote(note)
    suspend fun updateNote(note : Notes) = noteDao.updateNote(note)
}