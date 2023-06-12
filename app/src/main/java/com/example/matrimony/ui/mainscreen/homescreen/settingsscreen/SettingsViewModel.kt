package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.PrivacySettings
import com.example.matrimony.db.repository.AccountRepository
import com.example.matrimony.db.repository.PrivacySettingsRepository
import com.example.matrimony.db.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountsRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val privacySettingsRepository: PrivacySettingsRepository
) :
    ViewModel() {


    var userId = -1

    suspend fun isPasswordValid(userId: Int, password: String): Boolean {
        return accountsRepository.isPasswordValid(userId, password)
    }

    fun updatePassword(userId: Int, newPassword: String) {
        viewModelScope.launch {
            accountsRepository.updatePassword(userId, newPassword)
        }
    }

    fun updateMobile(userId: Int, newMobile: String) {
        viewModelScope.launch {
            accountsRepository.updateMobile(userId, newMobile)
        }
    }

    fun deleteAccount(userId: Int) {
        viewModelScope.launch {
            accountsRepository.deleteAccount(userId)
            userRepository.deleteAccount(userId)
        }
    }

    suspend fun addPrivacySettings(privacySettings: PrivacySettings) {
        privacySettingsRepository.addPrivacySettings(privacySettings)
    }

    suspend fun getPrivacySettings(userId: Int): LiveData<PrivacySettings> {
        return privacySettingsRepository.getPrivacySettings(userId)
    }


    fun updatePrivacySettings(
        userId: Int,
        viewProfile: String,
        viewContactNum: String,
        viewAlbum: String
    ) {
        viewModelScope.launch {

            privacySettingsRepository.updatePrivacySettings(
                userId,
                viewProfile,
                viewContactNum,
                viewAlbum
            )
        }
    }


    suspend fun removePrivacySettings(userId: Int) {
        privacySettingsRepository.removePrivacySettings(userId)
    }
}