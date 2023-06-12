package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.repositories

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.ShortlistsDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.ShortlistsRepository


class ShortlistsRepositoryImpl(
    private val shortlistsDao: ShortlistsDao
) : ShortlistsRepository {
    override fun getShortlistedProfiles(userId: Int): LiveData<List<Int>> {
        return shortlistsDao.getShortlistedProfiles(userId)
    }

    override suspend fun removeShortlist(userId: Int, shortlistedUserId: Int) {
        shortlistsDao.removeShortlist(userId, shortlistedUserId)
    }
}