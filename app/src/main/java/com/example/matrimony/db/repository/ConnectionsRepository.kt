package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import com.example.matrimony.db.dao.AccountDao
import com.example.matrimony.db.dao.ConnectionsDao
import com.example.matrimony.db.dao.UserDao
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.models.UserData

class ConnectionsRepository(
    private val connectionsDao: ConnectionsDao,
    private val accountsDao:AccountDao,
    private val userDao:UserDao
) {

    suspend fun addConnection(connection:Connections){
        connectionsDao.addConnection(connection)
    }

    suspend fun removeConnection(userId: Int, connectedUserId: Int){
        connectionsDao.removeConnection(userId, connectedUserId)
    }

    suspend fun setConnectionStatus(userId:Int, connectedUserId:Int, status:String){
        connectionsDao.setConnectionStatus(userId,connectedUserId,status)
    }

    suspend fun getConnectedUserIds(userId: Int): LiveData<List<Int>>{
        return connectionsDao.getConnectedUserIds(userId)
    }

    suspend fun getConnectionRequests(userId: Int): LiveData<List<Int>>{
        return connectionsDao.getConnectionRequests(userId)
    }

    suspend fun isRequestsPending(userId: Int):Boolean{
        return connectionsDao.isRequestsPending(userId)
    }

    suspend fun isConnectionAvailable(userId:Int,connectedUserId:Int):Boolean{
        return connectionsDao.isConnectionAvailable(userId, connectedUserId)
    }

    suspend fun getConnectionStatus(userId:Int,connectedUserId:Int):LiveData<String?>{
        return connectionsDao.getConnectionStatus(userId, connectedUserId)
    }

    suspend fun getConnectionDetails(userId: Int, connectedUserId: Int): LiveData<Connections?>{
        return connectionsDao.getConnectionDetails(userId, connectedUserId)
    }

    suspend fun getUserMobile(userId:Int):LiveData<String>{
        return accountsDao.getUserMobile(userId)
    }

    suspend fun getUserData(userId: Int): UserData {
        return userDao.getUserData(userId)
    }


    suspend fun getConnectedUsers(userId: Int): LiveData<List<UserData>>{
        return connectionsDao.getConnectedUsers(userId)
    }


}