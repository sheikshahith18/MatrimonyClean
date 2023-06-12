package com.example.matrimony.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.matrimony.BITMAP_TAG
import com.example.matrimony.TAG
import com.example.matrimony.db.dao.AlbumDao
import com.example.matrimony.db.entities.Album

class AlbumRepository(private val albumDao: AlbumDao) {

    suspend fun getProfilePic(userId: Int):LiveData<Album?>{
        return albumDao.getProfilePic(userId)
    }

    suspend fun addAlbum(album: Album){
        albumDao.addAlbum(album)
        Log.i(BITMAP_TAG,"album added ${album.user_id}")
    }


    suspend fun removePicture(userId: Int,imageId: Int){
        albumDao.removePicture(userId, imageId)
    }


    suspend fun getImage(userId:Int,imageId:Int): LiveData<Album>{
        return albumDao.getImage(userId, imageId)
    }

    suspend fun getUserAlbum(userId:Int): LiveData<List<Album?>?>{
        return albumDao.getUserAlbum(userId)
    }

    suspend fun getUserAlbumCount(userId: Int):LiveData<Int>{
        return albumDao.getUserAlbumCount(userId)
    }
}