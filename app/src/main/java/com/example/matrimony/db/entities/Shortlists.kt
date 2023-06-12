package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shortlists",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["user_id"],
            childColumns = ["shortlisted_user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Shortlists(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val user_id: Int,
    val shortlisted_user_id: Int,
)
