package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases

interface RemoveShortlistUseCase {
    suspend operator fun invoke(userId: Int, shortlistedUserId: Int)
}