package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases

import androidx.lifecycle.LiveData

interface GetUserAlbumCountUseCase {
    suspend operator fun invoke(userId: Int): LiveData<Int>
}