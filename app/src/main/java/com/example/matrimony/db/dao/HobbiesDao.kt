package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matrimony.db.entities.Hobbies

@Dao
interface HobbiesDao {
//    @PrimaryKey(autoGenerate = true) val id: Int,
//    val user_id: Int,
//    val hobby: String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHobby(hobby:Hobbies)


    @Query("DELETE FROM hobbies WHERE user_id= :userId AND hobby= :hobby")
    suspend fun removeHobby(userId:Int,hobby:String)


    @Query("SELECT hobby FROM hobbies WHERE user_id= :userId")
    fun getHobbies(userId:Int):LiveData<List<String>>

    @Query("DELETE FROM hobbies WHERE user_id= :userId")
    suspend fun removeAllHobbies(userId: Int)
}