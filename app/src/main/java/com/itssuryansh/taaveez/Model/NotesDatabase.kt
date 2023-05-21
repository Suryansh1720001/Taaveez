package com.itssuryansh.taaveez.Model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itssuryansh.taaveez.Data.NotesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [NotesEntity::class],version=2)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun NotesDao(): NotesDao

    //creating a callback for room database

    private class sRoomDatabase(private val scope:CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)


            //to clear all the contents before creating the table
            INSTANCE?.let{
                scope.launch {
                    it.NotesDao().deleteAll()
                }
            }
        }
    }


    companion object{

        @Volatile
        private var INSTANCE : NotesDatabase? = null

        fun getInstance(context: Context,scope: CoroutineScope): NotesDatabase {
            Log.d("tag", "getInstance of db is executed ")
            synchronized(this){
                var instance = INSTANCE

                if(instance==null){
                    instance = Room.databaseBuilder(
                        context,
                        NotesDatabase::class.java,
                        "notes_database"
                    ).addCallback(sRoomDatabase(scope))
                        .build()

                    INSTANCE = instance
                }
                Log.d("tag", "getInstance of db is returned ")

                return instance
            }
        }
    }
}