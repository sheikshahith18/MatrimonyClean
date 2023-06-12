package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories

import androidx.lifecycle.LiveData

interface AlbumRepository{
    suspend fun getUserAlbumCount(userId: Int):LiveData<Int>
}