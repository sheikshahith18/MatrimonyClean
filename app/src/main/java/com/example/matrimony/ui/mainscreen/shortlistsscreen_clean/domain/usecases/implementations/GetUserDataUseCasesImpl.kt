package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations

import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.UserRepository
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetUserDataUseCase
import javax.inject.Inject

class GetUserDataUseCasesImpl @Inject constructor(private val userRepository: UserRepository):GetUserDataUseCase{
    override suspend operator fun invoke(userId: Int):UserData=
        userRepository.getUserData(userId)
}