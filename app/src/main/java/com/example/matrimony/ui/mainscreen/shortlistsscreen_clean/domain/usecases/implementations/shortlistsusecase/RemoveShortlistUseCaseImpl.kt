package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase

import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.ShortlistsRepository
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.RemoveShortlistUseCase
import javax.inject.Inject

class RemoveShortlistUseCaseImpl @Inject constructor(private val shortlistsRepository: ShortlistsRepository) :
    RemoveShortlistUseCase {
    override suspend operator fun invoke(userId: Int, shortlistedUserId: Int): Unit =
        shortlistsRepository.removeShortlist(userId, shortlistedUserId)
}