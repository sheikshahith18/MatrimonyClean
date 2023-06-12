package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase

import androidx.lifecycle.LiveData
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.ShortlistsRepository
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetShortlistedProfilesUseCase
import javax.inject.Inject

class GetShortlistedProfilesUseCaseImpl @Inject constructor(private val shortlistsRepository: ShortlistsRepository) :
    GetShortlistedProfilesUseCase {
    override operator fun invoke(userId: Int): LiveData<List<Int>> =
        shortlistsRepository.getShortlistedProfiles(userId)

}