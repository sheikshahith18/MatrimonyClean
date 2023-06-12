package com.example.matrimony.ui.mainscreen.connectionsscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.db.repository.ConnectionsRepository
import com.example.matrimony.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionsViewModel
@Inject constructor(private val connectionsRepository: ConnectionsRepository) : ViewModel() {


//    val mobileButton=MutableLiveData<Unit?>()


    var gender = ""
    var userId = -1

    val removeFromConnections = mutableListOf<Int>()
    val sendConnectionsTo = mutableListOf<Int>()

    val removeConnection1= mutableListOf<Int>()

    var currentSelectedPage = 0

    fun addConnection(connection: Connections) {
        viewModelScope.launch {
            connectionsRepository.addConnection(connection)
        }
    }

    fun removeConnection(connectedUserId: Int) {
        viewModelScope.launch {
            connectionsRepository.removeConnection(
                this@ConnectionsViewModel.userId,
                connectedUserId
            )
        }
    }

    fun setConnectionStatus(connectedUserId:Int,status: String) {
        viewModelScope.launch {
            connectionsRepository.setConnectionStatus(this@ConnectionsViewModel.userId,connectedUserId, status)
        }
    }

    suspend fun isRequestsPending(userId: Int):Boolean{
        return connectionsRepository.isRequestsPending(userId)
    }

    suspend fun getConnectionStatus(userId:Int,connectedUserId:Int):LiveData<String?>{
        return connectionsRepository.getConnectionStatus(userId, connectedUserId)
    }

    suspend fun getConnectionDetails(userId: Int, connectedUserId: Int): LiveData<Connections?>{
        return connectionsRepository.getConnectionDetails(userId, connectedUserId)
    }

    suspend fun getConnectedUserIds(userId: Int): LiveData<List<Int>> {
        return connectionsRepository.getConnectedUserIds(userId)
    }

    suspend fun getConnectionRequests(userId: Int): LiveData<List<Int>> {
        return connectionsRepository.getConnectionRequests(userId)
    }

    suspend fun getUserMobile(userId: Int): LiveData<String> {
        return connectionsRepository.getUserMobile(userId)
    }

    suspend fun getUserData(userId: Int): UserData {
        return connectionsRepository.getUserData(userId)
    }

    suspend fun getConnectedUsers(userId: Int): LiveData<List<UserData>>{
        return connectionsRepository.getConnectedUsers(userId)
    }

}