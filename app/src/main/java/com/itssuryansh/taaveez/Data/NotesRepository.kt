package com.itssuryansh.taaveez.Data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.itssuryansh.taaveez.Model.NotesDatabase
import com.itssuryansh.taaveez.Model.NotesEntity
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val dbInstance:NotesDatabase) {

     fun fetchAllNotes() : LiveData<List<NotesEntity>> {
         return dbInstance.NotesDao().fetchAllNotes().asLiveData()
     }

    //insert
    @WorkerThread
    suspend fun insertNotes(notesEntity: NotesEntity){
        dbInstance.NotesDao().insert(notesEntity)
    }

    @WorkerThread
    suspend fun updateNotes(notesEntity: NotesEntity){
        dbInstance.NotesDao().update(notesEntity)
    }
    //delete unique note
    @WorkerThread
    suspend fun deleteUniqueNotes(notesEntity: NotesEntity){
        dbInstance.NotesDao().delete(notesEntity)
    }

    @WorkerThread
    suspend fun deleteAllNotes(){
        dbInstance.NotesDao().deleteAll()
    }

    fun getNoteById(id : Int):Flow<NotesEntity>{
        return dbInstance.NotesDao().fetchNotesById(id)
    }

}