package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.data.repositories

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.AlbumDao
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.AlbumRepository

class AlbumRepositoryImpl(private val albumDao: AlbumDao) :AlbumRepository{
    override suspend fun getUserAlbumCount(userId: Int):LiveData<Int>{
        return albumDao.getUserAlbumCount(userId)
    }
}