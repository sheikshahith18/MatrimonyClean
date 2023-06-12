package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.repositories

import com.example.matrimony.db.dao.UserDao
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun getUserData(userId: Int): UserData {
        return userDao.getUserData(userId)
    }
}