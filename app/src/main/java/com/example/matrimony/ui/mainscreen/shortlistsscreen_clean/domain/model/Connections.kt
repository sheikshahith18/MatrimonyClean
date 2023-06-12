package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "connections")
data class Connections(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val user_id: Int,
    val connected_user_id: Int,
    val status: String
)
