package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "habits",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Habits(
    @PrimaryKey(autoGenerate = false)
    val user_id: Int,
    var drinking: String="Never",
    var smoking: String="Never",
    var food_type: String="Vegetarian",
)
