package com.example.matrimony.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = true) var user_id: Int=0,
    var email: String,
    var mobile_no: String,
    var password: String
)