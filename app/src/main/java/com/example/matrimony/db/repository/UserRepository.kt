package com.example.matrimony.db.repository

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.example.matrimony.db.AppDatabase
import com.example.matrimony.db.dao.AccountDao
import com.example.matrimony.db.dao.UserDao
import com.example.matrimony.db.entities.User
import com.example.matrimony.models.UserData
import java.util.*

class UserRepository(
    private val userDao: UserDao,
    private val accountDao: AccountDao,
//    private val connectionsDao: ConnectionsDao
) {

    suspend fun getUser(userId: Int): LiveData<User> {
        return userDao.getUser(userId)
    }

    suspend fun getNoOfUsers():Int{
        return userDao.getNoOfUsers()
    }

    suspend fun getUserData(userId: Int): UserData {
        return userDao.getUserData(userId)
    }

    suspend fun updateProfilePic(userId: Int, image: Bitmap?) {
        userDao.updateProfilePic(userId, image)
    }

    suspend fun getProfilePic(userId: Int): LiveData<Bitmap?> {
        return userDao.getProfilePic(userId)
    }

    suspend fun updateUser(
        userId: Int,
        name: String,
        dob: Date,
        religion: String,
        maritalStatus: String,
        noOfChildren: Int?,
        caste: String?,
        zodiac: String,
        star: String,
        state: String,
        city: String?,
        height: String,
        physicalStatus: String,
        education: String,
        employedIn: String,
        occupation: String,
        annualIncome: String,
        familyStatus: String,
        familyType: String,
        about: String
    ) {
        userDao.updateUser(
            userId,
            name,
            dob,
            religion,
            maritalStatus,
            noOfChildren,
            caste,
            zodiac,
            star,
            state,
            city,
            height,
            physicalStatus,
            education,
            employedIn,
            occupation,
            annualIncome,
            familyStatus,
            familyType,
            about
        )
    }

//    suspend fun getAllUsers(): LiveData<List<UserData>> {
//        return userDao.getAllUsers()
//    }

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun getNameOfUser(userId: Int): LiveData<String> {
        return userDao.getNameOfUser(userId)
    }

//    suspend fun getNoOfUsers(): Int {
//        return userDao.getNoOfUsers()
//    }

    suspend fun getUserGender(userId: Int): String {
        return userDao.getUserGender(userId)
    }

    suspend fun getUsersBasedOnGender(gender: String): LiveData<List<UserData>> {
        return userDao.getUsersBasedOnGender(gender)
    }


    suspend fun getFilteredUserData(
        gender: String,
        ageFrom: Int,
        ageTo: Int,
        heightArraySize: Int,
        heightArray: List<String>,
        maritalStatusSize: Int,
        maritalStatusArray: List<String>,
        educationArraySize: Int,
        educationArray: List<String>,
        employedInSize: Int,
        employedInArray: List<String>,
        occupationArraySize: Int,
        occupationArray: List<String>,
        religionArraySize: Int,
        religionArray: List<String>,
        casteArraySize: Int,
        casteArray: List<String>,
        starArraySize: Int,
        starArray: List<String>,
        zodiacArraySize: Int,
        zodiacArray: List<String>,
        stateArraySize: Int,
        stateArray: List<String>,
        cityArraySize: Int,
        cityArray: List<String>,
        nameAscFlag: Int,
        nameDescFlag: Int,
        ageAscFlag: Int,
        ageDescFlag: Int,
        idAscFlag: Int,
        idDescFlag: Int,
        searchText:String
    ): LiveData<List<UserData>> {
        return userDao.getFilteredUserData(
            gender,
            ageFrom,
            ageTo,
            heightArraySize,
            heightArray,
            maritalStatusSize,
            maritalStatusArray,
            educationArraySize,
            educationArray,
            employedInSize,
            employedInArray,
            occupationArraySize,
            occupationArray,
            religionArraySize,
            religionArray,
            casteArraySize,
            casteArray,
            starArraySize,
            starArray,
            zodiacArraySize,
            zodiacArray,
            stateArraySize,
            stateArray,
            cityArraySize,
            cityArray,
            nameAscFlag,
            nameDescFlag,
            ageAscFlag,
            ageDescFlag,
            idAscFlag,
            idDescFlag,
            searchText
        )
    }



    suspend fun getUserMobile(userId: Int): LiveData<String> {
        return accountDao.getUserMobile(userId)
    }

    suspend fun updateMobile(userId: Int, newMobile: String) {
        accountDao.updateMobile(userId, newMobile)
    }


    suspend fun deleteAccount(userId: Int) {
        userDao.deleteUser(userId)
    }

}