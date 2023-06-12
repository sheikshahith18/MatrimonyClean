package com.example.matrimony.db.repository

import android.util.Log
import com.example.matrimony.BITMAP_TAG
import com.example.matrimony.db.dao.AccountDao
import com.example.matrimony.db.dao.UserDao
import com.example.matrimony.db.entities.Account
import com.example.matrimony.db.entities.User


class InjectDataRepository(
    private val accountDao: AccountDao,
    private val userDao: UserDao,
) {

    suspend fun addAccount(account:Account){
        accountDao.addAccount(account)
    }

    suspend fun addUser(user:User){
        userDao.addUser(user)
        Log.i(BITMAP_TAG,"user added ${user.user_id}")
    }

}