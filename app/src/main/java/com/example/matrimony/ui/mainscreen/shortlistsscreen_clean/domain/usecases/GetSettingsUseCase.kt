package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases

import androidx.lifecycle.LiveData
import com.example.matrimony.db.entities.PrivacySettings

interface GetSettingsUseCase {
    suspend operator fun invoke(userId: Int): LiveData<PrivacySettings>
}