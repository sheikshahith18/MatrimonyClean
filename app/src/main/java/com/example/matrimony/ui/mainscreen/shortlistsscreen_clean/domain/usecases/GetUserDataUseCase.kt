package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases

import com.example.matrimony.models.UserData

interface GetUserDataUseCase {
    suspend operator fun invoke(userId: Int): UserData
}