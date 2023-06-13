package com.itssuryansh.taaveez.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itssuryansh.taaveez.data.database.NotesDao
import com.itssuryansh.taaveez.domain.model.NotesEntity
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
    private val createNoteUseCase: CreateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val notesDao: NotesDao
) : ViewModel() {
    private val _notesList = MutableStateFlow<List<NotesEntity>>(emptyList())
    val notesList: StateFlow<List<NotesEntity>> = _notesList
    private val _notesLiveData = MutableLiveData<NotesEntity>()
    val notesLiveData: LiveData<NotesEntity> = _notesLiveData
    fun createNotes(notesEntity: NotesEntity) {
        viewModelScope.launch {
            createNoteUseCase
        }
    }

    fun updateNotes(notesEntity: NotesEntity) {
        viewModelScope.launch {
            updateNoteUseCase
        }
    }

    fun deleteNotes(notesEntity: NotesEntity) {
        viewModelScope.launch {
            deleteNoteUseCase
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            val notes = getAllNotesUseCase.invoke().first()
            _notesList.value = notes
        }
    }

//    fun getNotesById(id: Int) {
//        viewModelScope.launch {
//            notesDao.fetchNotesById(id).collect {
//                _notesLiveData.value = it
//            }
//        }
//    }
fun getNotesById(id: Int): Flow<NotesEntity?> {
    return notesDao.fetchNotesById(id)
}
}