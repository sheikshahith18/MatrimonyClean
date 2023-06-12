package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations

import androidx.lifecycle.LiveData
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.repositories.AlbumRepository
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetUserAlbumCountUseCase
import javax.inject.Inject

class GetUserAlbumCountUseCaseImpl @Inject constructor(private val albumRepository: AlbumRepository) :GetUserAlbumCountUseCase{
    override suspend operator fun invoke(userId: Int): LiveData<Int> =
        albumRepository.getUserAlbumCount(userId)
}