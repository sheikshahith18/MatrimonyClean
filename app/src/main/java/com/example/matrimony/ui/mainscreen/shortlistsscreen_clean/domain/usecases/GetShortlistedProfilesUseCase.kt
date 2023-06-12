package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases

import androidx.lifecycle.LiveData

interface GetShortlistedProfilesUseCase {
    operator fun invoke(userId: Int): LiveData<List<Int>>
}