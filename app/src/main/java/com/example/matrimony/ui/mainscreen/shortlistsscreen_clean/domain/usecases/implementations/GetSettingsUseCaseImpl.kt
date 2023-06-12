package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations

import androidx.lifecycle.LiveData
import com.example.matrimony.db.entities.PrivacySettings
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.PrivacySettingsRepository
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetSettingsUseCase
import javax.inject.Inject

class GetSettingsUseCaseImpl @Inject constructor(private val privacySettingsRepository: PrivacySettingsRepository) :GetSettingsUseCase{
    override suspend operator fun invoke(userId: Int): LiveData<PrivacySettings> =
        privacySettingsRepository.getPrivacySettings(userId)
}