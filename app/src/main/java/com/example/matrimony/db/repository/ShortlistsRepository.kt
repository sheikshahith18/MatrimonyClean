package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.matrimony.db.dao.ShortlistsDao
import com.example.matrimony.db.dao.UserDao
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.models.UserData


class ShortlistsRepository(private val userDao: UserDao, private val shortlistsDao: ShortlistsDao) {

    suspend fun addShortlist(shortlist: Shortlists) {
        shortlistsDao.addShortlist(shortlist)
    }

    fun getShortlistedProfiles(userId: Int): LiveData<List<Int>> {
        return shortlistsDao.getShortlistedProfiles(userId)
    }

    suspend fun removeShortlist(userId: Int, shortlistedUserId: Int) {
        shortlistsDao.removeShortlist(userId, shortlistedUserId)
    }

    suspend fun getShortlistedStatus(userId: Int, shortlistedUserId: Int): LiveData<Boolean> {
        return shortlistsDao.getShortlistedStatus(userId, shortlistedUserId)
    }

    suspend fun getUserData(userId: Int): UserData {
        return userDao.getUserData(userId)
    }

}