package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.repositories

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.ConnectionsDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.ConnectionsRepository

class ConnectionsRepositoryImpl(
    private val connectionsDao: ConnectionsDao,
) : ConnectionsRepository {
    override suspend fun getConnectionStatus(userId: Int, connectedUserId: Int): LiveData<String?> {
        return connectionsDao.getConnectionStatus(userId, connectedUserId)
    }
}