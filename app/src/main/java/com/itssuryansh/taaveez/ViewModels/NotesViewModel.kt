package com.itssuryansh.taaveez.ViewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itssuryansh.taaveez.Data.NotesRepository
import com.itssuryansh.taaveez.Model.NotesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotesViewModel(val notesRepository: NotesRepository) :ViewModel() {


    fun getAllNotesFromViewModel(): LiveData<List<NotesEntity>> {
       return notesRepository.fetchAllNotes()
    }

    fun getUniqueNoteFromViewModel(id:Int):Flow<NotesEntity>{
        return notesRepository.getNoteById(id)
    }

    fun deleteUniqueNoteFromViewModel(entity: NotesEntity)=viewModelScope.launch {
         notesRepository.deleteUniqueNotes(entity)
    }

    fun deleteAllFromViewModel() = viewModelScope.launch {
        notesRepository.deleteAllNotes()
    }

    fun insertNewNotesToViewModel(notesEntity: NotesEntity) = viewModelScope.launch(Dispatchers.IO) {
        notesRepository.insertNotes(notesEntity)
    }

    fun updateNotesToViewModel(notesEntity: NotesEntity) = viewModelScope.launch(Dispatchers.IO) {
        notesRepository.updateNotes(notesEntity)
    }
}

