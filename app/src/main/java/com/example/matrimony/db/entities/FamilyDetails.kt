package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "family_details")
data class FamilyDetails (
    @PrimaryKey
    val user_id:Int,
    var fathers_name:String,
    var mothers_name:String,
    var no_of_brothers:Int,
    var married_brothers:Int?,
    var no_of_sisters:Int,
    var married_sisters:Int?
)