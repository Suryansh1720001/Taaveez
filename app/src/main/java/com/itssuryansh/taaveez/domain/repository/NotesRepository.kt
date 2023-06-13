package com.itssuryansh.taaveez.domain.repository

import com.itssuryansh.taaveez.data.database.NotesDao
import com.itssuryansh.taaveez.domain.model.NotesEntity
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun insert(notesEntity: NotesEntity)


    suspend fun update(notesEntity: NotesEntity)

    suspend fun delete(notesEntity: NotesEntity)

    fun fetchAllNotes(): Flow<List<NotesEntity>>

    fun fetchNotesById(id: Int): Flow<NotesEntity>


}