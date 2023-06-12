package com.example.matrimony.ui.loginscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.TAG
import com.example.matrimony.db.entities.Account
import com.example.matrimony.db.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    private var _userId = MutableLiveData<Int>(-1)
    var userId: LiveData<Int> = _userId



    suspend fun getUserUsingEmailOrMobile(info:String):Int{
        return loginRepository.getUserByEmailOrMobile(info)
    }

    fun setUserIdByMobile(mobile: String) {
        Log.i(TAG, "before ${_userId.value}   ${userId.value}")
        viewModelScope.launch {
            _userId.value = loginRepository.getUserUsingMobile(mobile)
            Log.i(TAG, "after ${_userId.value}   ${userId.value}")
        }
    }


    suspend fun isCredentialsMatched(info: String, password: String): Boolean {
        return loginRepository.isCredentialsMatched(info, password)
    }

    suspend fun isEmailExist(email: String): Boolean {
        return loginRepository.isEmailExist(email)
    }

    suspend fun isMobileExist(mobile: String): Boolean {
        return loginRepository.isMobileExist(mobile)
    }

    suspend fun login(info: String, password: String): Boolean {
        return loginRepository.login(info, password) != -1
    }

    suspend fun getUserByMobile(mobile: String): Int {
        return loginRepository.getUserUsingMobile(mobile)
    }

    fun updatePassword(userId: Int, password: String) {
        viewModelScope.launch {
            Log.i(TAG, loginRepository.getPassword(userId).toString())
            loginRepository.updatePassword(userId, password)
            Log.i(TAG, "Password Updated")
            Log.i(TAG, loginRepository.getPassword(userId).toString())
        }
    }

}