package com.example.vit20bps1033ass3.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vit20bps1033ass3.Dao.NoteDao
import com.example.vit20bps1033ass3.model.Notes


@Database(entities = [Notes::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun notesDao() : NoteDao

    companion object{
        @Volatile
        private var INSTANCE : NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(true){
                val instance = Room.databaseBuilder(context.applicationContext,NoteDatabase::class.java,"NotesDatabase").build()
                INSTANCE = instance
                instance
            }
        }
    }
}