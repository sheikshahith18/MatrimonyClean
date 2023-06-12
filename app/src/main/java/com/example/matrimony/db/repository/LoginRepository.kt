package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.AccountDao
import com.example.matrimony.db.entities.Account

class LoginRepository(private val accountDao: AccountDao) {

    suspend fun addAccount(account: Account) {
        accountDao.addAccount(account)
    }

    suspend fun getUserUsingMail(email: String): Int {
        return accountDao.getUserByEmail(email)
    }

    suspend fun getUserUsingMobile(mobile: String): Int {
        return accountDao.getUserByMobile(mobile)
    }

    suspend fun getUserByEmailOrMobile(info:String):Int{
        return accountDao.getUserByEmailOrMobile(info)
    }

    suspend fun deleteAccount(userId: Int) {
        accountDao.deleteAccount(userId)
    }

    suspend fun getAllAccounts(): LiveData<List<Account>> {
        return accountDao.getAllAccounts()
    }


    suspend fun isEmailExist(email: String): Boolean {
        return accountDao.isEmailExist(email)
    }

    suspend fun isMobileExist(mobile: String): Boolean {
        return accountDao.isMobileExist(mobile)
    }

    suspend fun login(info: String, password: String): Int {
        if (accountDao.isEmailAndPasswordMatched(info, password)) {
            return accountDao.getUserByEmail(info)
        } else if (accountDao.isMobileAndPasswordMatched(info, password)) {
            return accountDao.getUserByMobile(info)
        }
        return -1
    }

    suspend fun isCredentialsMatched(info: String, password: String): Boolean {
        return accountDao.isCredentialsMatched(info, password)
    }

    suspend fun updatePassword(userId: Int, password: String) {
        accountDao.updatePassword(userId, password)
    }

    suspend fun getPassword(userId: Int): String {
        return accountDao.getPassword(userId)
    }
}
