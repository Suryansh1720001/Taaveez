package com.itssuryansh.taaveez.data.repository

import com.itssuryansh.taaveez.data.database.NotesDao
import com.itssuryansh.taaveez.domain.model.NotesEntity
import com.itssuryansh.taaveez.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(private val notesDao: NotesDao) : NotesRepository {
    override suspend fun insert(NotesEntity: NotesEntity) {
        notesDao.insert(NotesEntity)
    }

    override suspend fun update(NotesEntity: NotesEntity) {
        notesDao.update(NotesEntity)
    }

    override suspend fun delete(NotesEntity: NotesEntity) {
        notesDao.delete(NotesEntity)
    }

    override fun fetchAllNotes(): Flow<List<NotesEntity>> {
        return notesDao.fetchAllNotes()
    }

    override fun fetchNotesById(id: Int): Flow<NotesEntity> {
        return notesDao.fetchNotesById(id)
    }
}
