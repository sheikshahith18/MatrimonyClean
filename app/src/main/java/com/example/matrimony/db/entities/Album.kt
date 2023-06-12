package com.example.matrimony.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(
    tableName = "album",
)
data class Album(
    @PrimaryKey(autoGenerate = true) val image_id: Int,
    val user_id: Int,
    val image: Bitmap?,
    var isProfilePic:Boolean=false
){

}
