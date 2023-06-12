package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases

import androidx.lifecycle.LiveData

interface GetConnectionStatusUseCase {
    suspend operator fun invoke(userId:Int,connectedUserId:Int):LiveData<String?>
}