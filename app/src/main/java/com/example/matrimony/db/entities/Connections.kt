package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "connections",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("connected_user_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

data class Connections(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val user_id: Int,
    val connected_user_id: Int,
    val status: String
)
