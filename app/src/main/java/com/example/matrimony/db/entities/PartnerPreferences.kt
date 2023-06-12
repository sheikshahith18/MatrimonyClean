package com.example.matrimony.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.matrimony.db.converters.BitmapConverter
import com.example.matrimony.db.converters.ListConverter


@Entity(
    tableName = "partner_preferences",
)
data class PartnerPreferences(
    @PrimaryKey(autoGenerate = false)
    val user_id: Int,
    var age_from: Int=18,
    var age_to: Int=70,
    var height_from: String="4 ft 6 in",
    var height_to: String="6 ft",
    @TypeConverters(ListConverter::class)
    var marital_status: List<String>?=null,
    @TypeConverters(ListConverter::class)
    var education: List<String>? = null,
    @TypeConverters(ListConverter::class)
    var employed_in: List<String>? = null,
    @TypeConverters(ListConverter::class)
    var occupation: List<String>? = null,
    var religion: String? =null,
    @TypeConverters(ListConverter::class)
    var caste: List<String>? = null,
    @TypeConverters(ListConverter::class)
    var star: List<String>? = null,
    @TypeConverters(ListConverter::class)
    var zodiac: List<String>? = null,
    var state: String? = null,
    @TypeConverters(ListConverter::class)
    var city: List<String>? = null
)
