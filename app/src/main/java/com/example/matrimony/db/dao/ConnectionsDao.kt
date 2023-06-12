package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.models.UserData

@Dao
interface ConnectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addConnection(connection: Connections)

    @Query(
        "DELETE FROM connections " +
                "WHERE " +
                "((user_id = :userId AND connected_user_id = :connectedUserId) " +
                "OR (user_id = :connectedUserId AND connected_user_id = :userId))"
    )
    suspend fun removeConnection(userId: Int, connectedUserId: Int)

    @Query(
        "SELECT connected_user_id\n" +
                "FROM connections\n" +
                "WHERE user_id = :userId AND status = 'CONNECTED'\n" +
                "\n" +
                "UNION\n" +
                "\n" +
                "SELECT user_id\n" +
                "FROM connections\n" +
                "WHERE connected_user_id = :userId AND status = 'CONNECTED'"
    )
    fun getConnectedUserIds(userId: Int): LiveData<List<Int>>

    @Query(
        "SELECT user_id\n" +
                "FROM connections\n" +
                "WHERE connected_user_id = :connectedUserId AND status = 'REQUESTED'\n"
    )
    fun getConnectionRequests(connectedUserId: Int): LiveData<List<Int>>


    @Query(
        "SELECT EXISTS (SELECT * FROM connections " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR  (user_id= :connectedUserId AND connected_user_id= :userId))"
    )
    suspend fun isConnectionAvailable(userId: Int, connectedUserId: Int): Boolean



    @Query("SELECT EXISTS (SELECT * FROM connections " +
            "WHERE (connected_user_id= :userId AND status='REQUESTED'))")
    suspend fun isRequestsPending(userId: Int):Boolean

    @Query(
        "SELECT status " +
                "FROM connections " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR (user_id= :connectedUserId AND connected_user_id= :userId)"
    )
    fun getConnectionStatus(userId: Int, connectedUserId: Int): LiveData<String?>

    @Query(
        "SELECT * " +
                "FROM connections " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR (user_id= :connectedUserId AND connected_user_id= :userId)"
    )
    fun getConnectionDetails(userId: Int, connectedUserId: Int): LiveData<Connections?>


    @Query(
        "UPDATE connections " +
                "SET status = :status " +
                "WHERE (user_id= :userId AND connected_user_id= :connectedUserId) " +
                "OR " +
                "(user_id= :connectedUserId AND connected_user_id= :userId)"
    )
    suspend fun setConnectionStatus(userId: Int, connectedUserId: Int, status: String)


    @Query(
        "SELECT user_id as userId,name,dob,religion,caste,state,city,height,profile_pic,education,occupation " +
                "FROM user " +
                "WHERE user_id IN " +
                "(" +
                "SELECT connected_user_id " +
                "FROM connections " +
                "WHERE user_id = :userId AND status = 'CONNECTED' " +
                "UNION " +
                "SELECT user_id " +
                "FROM connections " +
                "WHERE connected_user_id = :userId AND status = 'CONNECTED'" +
                ")"
    )
    fun getConnectedUsers(userId: Int): LiveData<List<UserData>>

//    @PrimaryKey
//    val id: Int=0,
//    val user_id: String,
//    val connected_user_id: String,
//    val status: String

}