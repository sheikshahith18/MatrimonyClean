package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.Album

@Dao
interface AlbumDao {

//    @PrimaryKey
//    val image_id: Int,
//    val user_id: Int,
//    val image: Bitmap,


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