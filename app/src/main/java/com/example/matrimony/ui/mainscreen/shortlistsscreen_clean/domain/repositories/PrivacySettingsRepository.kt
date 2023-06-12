package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories

import androidx.lifecycle.LiveData
import com.example.matrimony.db.entities.PrivacySettings


interface PrivacySettingsRepository{
    suspend fun getPrivacySettings(userId: Int): LiveData<PrivacySettings>
}