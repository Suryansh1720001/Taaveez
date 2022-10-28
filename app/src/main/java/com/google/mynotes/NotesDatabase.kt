package com.google.mynotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotesEntity::class],version=1)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun employeeDao():NotesDao
    companion object{

        @Volatile
        private var INSTANCE : NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase{

            synchronized(this){
                var instance = INSTANCE

                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java,
                        "notes_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }

        }

    }
}