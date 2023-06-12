package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.matrimony.db.dao.HabitsDao
import com.example.matrimony.db.entities.Habits

class HabitsRepository(private val habitsDao: HabitsDao) {

    suspend fun insertHabit(habits: Habits){
        habitsDao.insertHabit(habits)
    }

    suspend fun getUserHabits(userId:Int): LiveData<Habits?>{
        return habitsDao.getUserHabits(userId)
    }

    suspend fun updateHabits(userId:Int,drinking:String,smoking:String,foodType:String){
        habitsDao.updateHabits(userId,drinking, smoking, foodType)
    }


}