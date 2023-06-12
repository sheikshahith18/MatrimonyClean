package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.matrimony.db.dao.AccountDao
import com.example.matrimony.db.dao.UserDao
import com.example.matrimony.db.entities.Account
import com.example.matrimony.db.entities.User

class AccountRepository(
    private val accountDao: AccountDao,
    private val userDao: UserDao
) {

    suspend fun isEmailAlreadyExists(email: String): Boolean {
        return accountDao.isEmailExist(email)
    }

    suspend fun isMobileAlreadyExists(mobile: String): Boolean {
        return accountDao.isMobileExist(mobile)
    }

    suspend fun addAccount(account: Account) {
        accountDao.addAccount(account)
    }

    suspend fun deleteAccount(userId: Int){
        accountDao.deleteAccount(userId)
    }

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun getUserByEmail(email: String): Int {
        return accountDao.getUserByEmail(email)
    }

    suspend fun getUserGender(userId: Int): String {
        return userDao.getUserGender(userId)
    }

    suspend fun getUserByMobile(mobile: String): Int{
        return accountDao.getUserByMobile(mobile)

    }

    suspend fun getUser(userId: Int): LiveData<User> {
        return userDao.getUser(userId)
    }

    suspend fun updateAdditionalDetails(
        userId: Int,
        height: String,
        physicalStatus: String,
        maritalStatus: String,
        childrenCount: Int?,
        education: String,
        employedIn: String,
        occupation: String,
        annualIncome: String,
        familyStatus: String,
        familyType: String,
        about: String
    ) {
        userDao.updateAdditionalDetails(
            userId,
            height,
            physicalStatus,
            maritalStatus,
            childrenCount,
            education,
            employedIn,
            occupation,
            annualIncome,
            familyStatus,
            familyType,
            about
        )
    }

    suspend fun isPasswordValid(userId:Int,password:String):Boolean{
        return accountDao.isPasswordValid(userId, password)
    }
    suspend fun updatePassword(userId: Int, newPassword: String){
        accountDao.updatePassword(userId, newPassword)
    }

    suspend fun updateMobile(userId: Int, newMobile: String){
        accountDao.updateMobile(userId, newMobile)
    }


}