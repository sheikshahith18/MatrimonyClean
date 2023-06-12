package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.db.entities.Shortlists

@Dao
interface ShortlistsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShortlist(shortlist: Shortlists)

    @Query("SELECT shortlisted_user_id FROM shortlists WHERE user_id= :userId ORDER BY id DESC")
    fun getShortlistedProfiles(userId: Int): LiveData<List<Int>>

    @Query("DELETE FROM shortlists WHERE user_id= :userId AND shortlisted_user_id= :shortlistedUserId")
    suspend fun removeShortlist(userId: Int, shortlistedUserId: Int)


    @Query("SELECT EXISTS (SELECT * FROM shortlists WHERE user_id= :userId AND shortlisted_user_id= :shortlistedUserId)")
    fun getShortlistedStatus(userId:Int,shortlistedUserId:Int):LiveData<Boolean>

//    @PrimaryKey
//    val id: Int=0,
//    val user_id: String,
//    val shortlisted_user_id: String,

}