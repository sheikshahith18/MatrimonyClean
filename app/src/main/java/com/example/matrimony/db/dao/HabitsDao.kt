package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.Habits


@Dao
interface HabitsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habits:Habits)

    @Query("Select * FROM habits WHERE user_id= :userId")
    fun getUserHabits(userId:Int):LiveData<Habits?>


    @Query("UPDATE habits SET drinking= :drinking, smoking= :smoking, food_type= :foodType WHERE user_id= :userId")
    suspend fun updateHabits(userId:Int,drinking:String,smoking:String,foodType:String)
}