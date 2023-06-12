package com.example.matrimony.models

import android.graphics.Bitmap
import com.example.matrimony.utils.calculateAge
import java.util.*

data class UserData(
    val userId: Int ,
    val name: String,
    val dob: Date?,
    val religion: String?,
    val caste: String?,
    val state: String?,
    val city: String?,
    val height: String?,
    val profile_pic: Bitmap?,
    val education: String?,
    val occupation: String?,
) {
    val age: Int
        get() = calculateAge(dob!!)
}


data class UserData1(
    val userId: Int ,
    val name: String,
    val dob: Date?,
    val religion: String?,
    val caste: String?,
    val state: String?,
    val city: String?,
    val height: String?,
//    val profile_pic: Bitmap?,
    val education: String?,
    val occupation: String?,
) {
    val age: Int
        get() = calculateAge(dob!!)
}

