package com.itssuryansh.taaveez

import androidx.room.*
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

    @Query("select Labels from `Poem-table`")
    fun fetchAllLabels(): Flow<String>

    @Query("update `Poem-table` set Labels=:labels where id=:id")
    fun updateLabels(labels:String, id:Int)

}
