package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "shortlists")
data class Shortlists(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val user_id: Int,
    val shortlisted_user_id: Int,
)
