package com.itssuryansh.taaveez

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Poem-table")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Topic: String = "",
    val Poem: String = "",
    val Date: String = "",
    val CreatedDate: String = "",
)
