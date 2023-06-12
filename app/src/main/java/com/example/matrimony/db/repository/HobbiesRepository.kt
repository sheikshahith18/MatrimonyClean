package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.matrimony.db.dao.HobbiesDao
import com.example.matrimony.db.entities.Hobbies

class HobbiesRepository(private val hobbiesDao: HobbiesDao) {

    suspend fun addHobby(hobby: Hobbies){
        hobbiesDao.addHobby(hobby)
    }


    suspend fun removeHobby(userId:Int,hobby:String){
        hobbiesDao.removeHobby(userId, hobby)
    }

    suspend fun getHobbies(userId:Int): LiveData<List<String>>{
        return hobbiesDao.getHobbies(userId)
    }

    suspend fun removeAllHobbies(userId: Int){
        hobbiesDao.removeAllHobbies(userId)
    }
}