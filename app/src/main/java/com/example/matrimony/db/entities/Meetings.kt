package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "meetings",
)
data class Meetings(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    val sender_user_id: Int,
    val receiver_user_id: Int,
    val title: String,
    val date: Date,
    val time: String,
    val place: String,
    val status: String
)
