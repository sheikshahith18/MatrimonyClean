package com.example.matrimony.db.converters

import androidx.room.TypeConverter


class ListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        if(list==null)
            return null
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(string: String?): List<String>? {
        if(string==null)
            return null
        return string.split(",").map { it.trim() }
    }
}