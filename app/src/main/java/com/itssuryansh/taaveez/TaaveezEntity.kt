package com.itssuryansh.taaveez

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Taaveez-table")
data class TaaveezEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val Topic: String="",
    val Content: String="",
    val Date:String="",
    val CreatedDate:String="",
    val SmallDes : String?=null,
    val isComplete : Boolean =false,
    val favorite:Boolean=false,
    val image : String?=null

)