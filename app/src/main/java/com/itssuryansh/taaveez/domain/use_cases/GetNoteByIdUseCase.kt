package com.itssuryansh.taaveez.domain.use_cases

import com.itssuryansh.taaveez.domain.model.NotesEntity
import com.itssuryansh.taaveez.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    operator fun invoke(id: Int): Flow<NotesEntity> {
        return notesRepository.fetchNotesById(id)
    }
}