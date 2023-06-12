package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.repositories

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.PrivacySettingsDao
import com.example.matrimony.db.entities.PrivacySettings
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.PrivacySettingsRepository


class PrivacySettingsRepositoryImpl(private val privacySettingsDao: PrivacySettingsDao) :PrivacySettingsRepository{
    override suspend fun getPrivacySettings(userId: Int): LiveData<PrivacySettings> {
        return privacySettingsDao.getPrivacySettings(userId)
    }
}