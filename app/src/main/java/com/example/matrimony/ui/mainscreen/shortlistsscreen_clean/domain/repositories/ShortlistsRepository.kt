package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories

import androidx.lifecycle.LiveData


interface ShortlistsRepository{

    fun getShortlistedProfiles(userId: Int): LiveData<List<Int>>

    suspend fun removeShortlist(userId: Int, shortlistedUserId: Int)

}