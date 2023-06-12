package com.example.matrimony.ui.registerscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.TAG
import com.example.matrimony.db.entities.Account
import com.example.matrimony.db.entities.PrivacySettings
import com.example.matrimony.db.entities.User
import com.example.matrimony.db.repository.AccountRepository
import com.example.matrimony.db.repository.PrivacySettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val privacySettingsRepository: PrivacySettingsRepository

) : ViewModel() {

    var selectedReligion = ""
    var selectedCaste = ""
    var selectedMotherTongue = ""
    var selectedState = ""
    var selectedCity = ""

    var religionPosition = -1
    var castePosition = -1
    var languagePosition = -1
    var statePosition = -1
    var cityPosition = -1
    var maritalStatusPosition = -1

    var loaded=false
    var loadingStarted=false

    suspend fun isEmailAlreadyExists(email: String): Boolean {
        return accountRepository.isEmailAlreadyExists(email)
    }


    suspend fun isMobileAlreadyExiists(mobile: String): Boolean {
        return accountRepository.isMobileAlreadyExists(mobile)
    }

    suspend fun createAccount(account: Account,user:User) {
//        viewModelScope.launch {
        accountRepository.addAccount(account)
        val userId=accountRepository.getUserByMobile(account.mobile_no)
        user.user_id=userId
        accountRepository.addUser(user)
        Log.i(TAG,"singUpViewModel ${user.toString()}")
        privacySettingsRepository.addPrivacySettings(PrivacySettings(userId))
//        }
    }

    suspend fun addUser(user:User){
        accountRepository.addUser(user)
    }

    suspend fun getUserByEmail(email: String): Int {
        return accountRepository.getUserByEmail(email)
    }

    suspend fun getUserGender(userId: Int): String {
        return accountRepository.getUserGender(userId)
    }

    suspend fun getUser(userId: Int): LiveData<User> {
        return accountRepository.getUser(userId)
    }

    fun updateAdditionalDetails(
        userId: Int,
        height: String,
        physicalStatus: String,
        maritalStatus: String,
        childrenCount: Int?,
        education: String,
        employedIn: String,
        occupation: String,
        annualIncome: String,
        familyStatus: String,
        familyType: String,
        about: String
    ) {
        viewModelScope.launch {
            accountRepository.updateAdditionalDetails(
                userId,
                height,
                physicalStatus,
                maritalStatus,
                childrenCount,
                education,
                employedIn,
                occupation,
                annualIncome,
                familyStatus,
                familyType,
                about
            )
        }
    }

}