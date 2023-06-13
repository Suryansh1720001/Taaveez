package com.itssuryansh.taaveez.domain.use_cases

import android.util.Log
import com.itssuryansh.taaveez.domain.model.NotesEntity
import com.itssuryansh.taaveez.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

  class GetAllNotesUseCase @Inject constructor (private val notesRepository: NotesRepository) {
     operator fun invoke(): Flow<List<NotesEntity>> {
         Log.d("GetAllNotesUseCase", "Fetching notes from repository...")
         val notes = notesRepository.fetchAllNotes()
         Log.d("GetAllNotesUseCase", "Notes received: $notes")
         return notes    }
}