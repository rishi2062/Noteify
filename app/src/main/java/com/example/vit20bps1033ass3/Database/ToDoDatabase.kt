package com.example.vit20bps1033ass3.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vit20bps1033ass3.Dao.ToDoDao
import com.example.vit20bps1033ass3.model.ToDo


@Database(entities = [ToDo::class], version = 2)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            return INSTANCE ?: synchronized(true) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "ToDoDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}