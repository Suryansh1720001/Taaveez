package com.itssuryansh.taaveez.domain.use_cases

import com.itssuryansh.taaveez.domain.model.NotesEntity
import com.itssuryansh.taaveez.domain.repository.NotesRepository
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor (private val notesRepository: NotesRepository){
    suspend operator fun invoke(notesEntity: NotesEntity){
        notesRepository.insert(notesEntity)
    }
}