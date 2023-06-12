package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "hobbies",
)
data class Hobbies(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val user_id: Int,
    val hobby: String
)
