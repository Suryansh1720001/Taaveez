package com.itssuryansh.taaveez.database

import androidx.room.*
import com.itssuryansh.taaveez.TaaveezEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaaveezDao {

    @Insert
    suspend fun insert(TaaveezEntity : TaaveezEntity)

    @Update
    suspend fun update(TaaveezEntity : TaaveezEntity)

    @Delete
    suspend fun delete(TaaveezEntity : TaaveezEntity)

    @Query("select * from `Taaveez-table`")
    fun fetchAllContents(): Flow<List<TaaveezEntity>>

    @Query("select * from `Taaveez-table` where id=:id")
    fun fetchContentsById(id:Int):Flow<TaaveezEntity>

}