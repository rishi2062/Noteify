package com.example.vit20bps1033ass3.Dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.vit20bps1033ass3.model.Notes


@Dao
interface NoteDao  {

    @Insert
    suspend fun insertNote(note : Notes)

    @Update
    suspend fun  updateNote(note : Notes)

    @Delete
    suspend fun deleteNote(note : Notes)

    @Query("SELECT * FROM NOTES ORDER BY id")
    fun getAllNotes() : LiveData<List<Notes>>
}