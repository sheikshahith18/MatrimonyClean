package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.example.matrimony.db.dao.FamilyDetailsDao
import com.example.matrimony.db.entities.FamilyDetails

class FamilyDetailsRepository(private val familyDetailsDao:FamilyDetailsDao) {


    suspend fun getFamilyDetails(userId:Int): LiveData<FamilyDetails?>{
        return familyDetailsDao.getFamilyDetails(userId)
    }


    suspend fun setFamilyDetails(familyDetails: FamilyDetails){
        familyDetailsDao.setFamilyDetails(familyDetails)
    }
}