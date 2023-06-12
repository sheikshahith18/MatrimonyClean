package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matrimony.db.entities.PrivacySettings

@Dao
interface PrivacySettingsDao {

//    @PrimaryKey
//    val id: Int,
//    val user_id: Int,
//    val view_profile_pic: String,
//    val view_contact_num: String,
//    val view_my_album: String


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPrivacySettings(privacySettings: PrivacySettings)


    @Query("SELECT * FROM privacy_settings WHERE user_id= :userId")
    fun getPrivacySettings(userId: Int):LiveData<PrivacySettings>



    @Query("UPDATE privacy_settings SET view_profile_pic= :viewProfile, view_contact_num= :viewContactNum, view_my_album= :viewAlbum WHERE user_id= :userId")
    suspend fun updatePrivacySettings(
        userId: Int,
        viewProfile: String,
        viewContactNum: String,
        viewAlbum: String
    )


    @Query("DELETE FROM privacy_settings WHERE user_id= :userId")
    suspend fun removePrivacySettings(userId:Int)


}