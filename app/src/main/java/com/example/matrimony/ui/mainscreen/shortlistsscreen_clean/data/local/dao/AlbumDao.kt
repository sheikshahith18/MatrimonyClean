package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.Album

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlbum(album:Album)


    @Query("DELETE FROM album WHERE user_id= :userId AND image_id= :imageId ")
    suspend fun removePicture(userId: Int,imageId: Int)


    @Query("SELECT * FROM album WHERE user_id= :userId AND image_id= :imageId")
    fun getImage(userId:Int,imageId:Int):LiveData<Album>

    @Query("SELECT * FROM album WHERE user_id= :userId")
    fun getUserAlbum(userId:Int):LiveData<List<Album?>?>

    @Query("SELECT COUNT(*) FROM album WHERE user_id= :userId")
    fun getUserAlbumCount(userId: Int):LiveData<Int>

    @Query("SELECT * FROM album WHERE user_id= :userId and isProfilePic=true")
    fun getProfilePic(userId: Int):LiveData<Album?>

}