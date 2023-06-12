package com.example.matrimony.utils

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*



const val MY_SHARED_PREFERENCES = "MATRIMONY_APP_PREFERENCES"
const val CURRENT_USER_ID="CURRENT_USER_ID"
const val CURRENT_USER_GENDER="CURRENT_USER_GENDER"

fun calculateAge(date:Date): Int {
    val birthDate = Calendar.getInstance().apply {
        time = date
    }
    val currentDate = Calendar.getInstance()

    var age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
    if (currentDate.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}

fun getDateFromString(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.parse(dateString)
}