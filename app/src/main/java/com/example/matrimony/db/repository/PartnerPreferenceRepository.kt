package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.example.matrimony.db.dao.PartnerPreferenceDao
import com.example.matrimony.db.entities.PartnerPreferences

class PartnerPreferenceRepository(private val partnerPreferenceDao: PartnerPreferenceDao) {
    suspend fun getPartnerPreference(userId: Int): LiveData<PartnerPreferences> {
        return partnerPreferenceDao.getPartnerPreference(userId)
    }


    suspend fun addPreference(partnerPreferences: PartnerPreferences) {
        partnerPreferenceDao.addPreference(partnerPreferences)
    }

    suspend fun clearPreference(userId: Int){
        partnerPreferenceDao.clearPreference(userId)
    }


    suspend fun updatePartnerPreference(
        userId: Int,
        ageFrom: Int,
        ageTo: Int,
        heightFrom: String,
        heightTo: String,
        maritalStatus: String,
        education: List<String>,
        employedIn: List<String>,
        occupation: List<String>,
        religion: String,
        caste: List<String>,
        star: List<String>,
        zodiac: List<String>,
        state: String,
        city: List<String>
    ) {
        partnerPreferenceDao.updatePartnerPreference(
            userId,
            ageFrom,
            ageTo,
            heightFrom,
            heightTo,
            maritalStatus,
            education,
            employedIn,
            occupation,
            religion,
            caste,
            star,
            zodiac,
            state,
            city
        )
    }

    suspend fun isPreferenceSet(userId:Int):Boolean{
        return partnerPreferenceDao.isPreferenceSet(userId)
    }
}