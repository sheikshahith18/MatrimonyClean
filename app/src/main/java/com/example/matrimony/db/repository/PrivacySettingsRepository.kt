package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.dao.PrivacySettingsDao
import com.example.matrimony.db.entities.PrivacySettings
import dagger.hilt.android.AndroidEntryPoint


class PrivacySettingsRepository(private val privacySettingsDao: PrivacySettingsDao) {


    suspend fun addPrivacySettings(privacySettings: PrivacySettings) {
        privacySettingsDao.addPrivacySettings(privacySettings)
    }

    suspend fun getPrivacySettings(userId: Int): LiveData<PrivacySettings> {
        return privacySettingsDao.getPrivacySettings(userId)
    }


    suspend fun updatePrivacySettings(
        userId: Int,
        viewProfile: String,
        viewContactNum: String,
        viewAlbum: String
    ) {
        privacySettingsDao.updatePrivacySettings(userId, viewProfile, viewContactNum, viewAlbum)
    }


    suspend fun removePrivacySettings(userId: Int) {
        privacySettingsDao.removePrivacySettings(userId)
    }

}