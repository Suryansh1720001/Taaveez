package com.itssuryansh.taaveez.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itssuryansh.taaveez.Data.NotesRepository

@Suppress("UNCHECKED_CAST")
class NotesViewModelFactory(private val notesRepository: NotesRepository)
    : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(notesRepository) as T
    }
}