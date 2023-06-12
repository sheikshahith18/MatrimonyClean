package com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.PrivacySettings
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetConnectionStatusUseCase
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetSettingsUseCase
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetUserAlbumCountUseCase
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.GetUserDataUseCase
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.domain.usecases.implementations.shortlistsusecase.ShortlistsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShortlistsViewModel @Inject constructor(
    private val shortlistsUseCases: ShortlistsUseCases,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val getUserAlbumCountUseCase: GetUserAlbumCountUseCase,
    private val getUserDataUseCases: GetUserDataUseCase
):ViewModel(){

    var userId=-1
    var gender=""

    fun getShortlistedProfiles(userId: Int): LiveData<List<Int>> {

        return shortlistsUseCases.getShortlistedProfilesUseCase(userId)
    }

    fun removeShortlist(shortlistedUserId: Int) {
        viewModelScope.launch {
            shortlistsUseCases.removeShortlistUseCase(this@ShortlistsViewModel.userId,shortlistedUserId)
        }
    }

    suspend fun getUserData(userId: Int): UserData {
        return getUserDataUseCases(userId)
    }

    suspend fun getPrivacySettings(userId: Int): LiveData<PrivacySettings> {
        return getSettingsUseCase(userId)
    }

    suspend fun getConnectionStatus(userId: Int, connectedUserId: Int): LiveData<String?> {
        return getConnectionStatusUseCase(userId, connectedUserId)
    }

    suspend fun getUserAlbumCount(userId: Int): LiveData<Int> {
        return getUserAlbumCountUseCase(userId)
    }
}