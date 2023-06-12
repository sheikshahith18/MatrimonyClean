package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories

import androidx.lifecycle.LiveData

interface ConnectionsRepository{
    suspend fun getConnectionStatus(userId:Int,connectedUserId:Int):LiveData<String?>
}