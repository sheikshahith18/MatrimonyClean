package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations

import androidx.lifecycle.LiveData
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.ConnectionsRepository
import javax.inject.Inject

class GetConnectionStatusUseCaseImpl @Inject constructor(private val connectionsRepository: ConnectionsRepository) :com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetConnectionStatusUseCase{

    override suspend fun invoke(userId: Int, connectedUserId: Int): LiveData<String?> =
        connectionsRepository.getConnectionStatus(userId, connectedUserId)

}