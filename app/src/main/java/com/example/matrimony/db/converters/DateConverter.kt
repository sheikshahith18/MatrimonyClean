package com.example.matrimony.db.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromDate(date:Date):Long{
        return date.time.toLong()
    }

    @TypeConverter
    fun toDate(value:Long):Date{
        return Date(value)
    }
}