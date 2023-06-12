package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "privacy_settings", foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class PrivacySettings(
    @PrimaryKey
    val user_id: Int,
    val view_profile_pic: String = "Everyone",
    val view_contact_num: String = "Connections Only",
    val view_my_album: String = "Everyone"
)
