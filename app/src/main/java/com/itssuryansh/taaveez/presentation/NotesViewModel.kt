package com.itssuryansh.taaveez.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itssuryansh.taaveez.data.database.NotesDao
import com.itssuryansh.taaveez.data.database.NotesEntity
import com.itssuryansh.taaveez.domain.repository.NotesRepository
import com.itssuryansh.taaveez.domain.use_cases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
   private val notesRepository: NotesRepository,
    private val notesDao: NotesDao
) : ViewModel() {
    private val _notesList = MutableStateFlow<List<NotesEntity>>(emptyList())
    val notesList: StateFlow<List<NotesEntity>> = _notesList
    private val _notesLiveData = MutableLiveData<NotesEntity>()
    val notesLiveData: LiveData<NotesEntity> = _notesLiveData
    fun createNotes(notesEntity: NotesEntity) {
        viewModelScope.launch {
            notesRepository.insert(notesEntity)
        }
    }

    fun updateNotes(notesEntity: NotesEntity) {
        viewModelScope.launch {
            notesRepository.update(notesEntity)
        }
    }

    fun deleteNotes(notesEntity: NotesEntity) {
        viewModelScope.launch {
            notesRepository.delete(notesEntity)
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            val notes = notesRepository.fetchAllNotes().first()
            _notesList.value = notes
        }
    }


fun getNotesById(id: Int): Flow<NotesEntity?> {
    return notesDao.fetchNotesById(id)
}
}