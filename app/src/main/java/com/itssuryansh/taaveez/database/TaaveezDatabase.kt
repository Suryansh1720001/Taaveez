package com.itssuryansh.taaveez.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itssuryansh.taaveez.TaaveezEntity

@Database(entities = [TaaveezEntity::class],version=3)
abstract class TaaveezDatabase: RoomDatabase() {

    abstract fun TaaveezDao(): TaaveezDao
    companion object{

        @Volatile
        private var INSTANCE : TaaveezDatabase? = null

        fun getInstance(context: Context): TaaveezDatabase {

            synchronized(this){
                var instance = INSTANCE

                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaaveezDatabase::class.java,
                        "taaveez_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}