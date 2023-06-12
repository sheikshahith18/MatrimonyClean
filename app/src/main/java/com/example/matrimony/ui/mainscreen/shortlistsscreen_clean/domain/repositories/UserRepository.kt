package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories

import com.example.matrimony.models.UserData

interface UserRepository{
    suspend fun getUserData(userId: Int): UserData
}