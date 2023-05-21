package com.itssuryansh.taaveez.Data

import androidx.room.*
import com.itssuryansh.taaveez.Model.NotesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun insert(NotesEntity : NotesEntity)

    @Update
    suspend fun update(NotesEntity : NotesEntity)

    @Delete
    suspend fun delete(NotesEntity : NotesEntity)

    @Query("select * from `Poem-table`")
    fun fetchAllNotes(): Flow<List<NotesEntity>>

    @Query("select * from `Poem-table` where id=:id")
    fun fetchNotesById(id:Int):Flow<NotesEntity>

    @Query("DELETE FROM `Poem-table`")
    suspend fun deleteAll()

}