package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "privacy_settings")
data class PrivacySettings(
    @PrimaryKey
    val user_id: Int,
    val view_profile_pic: String = "Everyone",
    val view_contact_num: String = "Connections Only",
    val view_my_album: String = "Everyone"
)
