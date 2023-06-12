package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.Meetings
import java.util.*

@Dao
interface MeetingsDao {

//    val sender_user_id: Int,
//    val receiver_user_id: Int,
//    val title: String,
//    val date: Date,
//    val time: String,
//    val place: String,
//    val status: String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewMeeting(meetings: Meetings)


    @Query(
        "DELETE FROM meetings " +
                "WHERE (sender_user_id= :senderUserId AND receiver_user_id= :receiverUserId) " +
                "OR (sender_user_id= :receiverUserId AND receiver_user_id= :senderUserId)"
    )
    suspend fun deleteMeeting(senderUserId: Int, receiverUserId: Int)

    @Query(
        "SELECT * FROM meetings " +
                "WHERE( sender_user_id= :userId AND  status = 'UPCOMING') " +
                "OR (receiver_user_id= :userId  AND status = 'UPCOMING')"
    )
    fun getUpcomingMeetings(userId: Int): LiveData<Meetings>

    @Query("SELECT * FROM meetings WHERE id= :meetingId")
    fun getMeetingOnId(meetingId: Int): LiveData<Meetings>

    @Query(
        "SELECT * " +
                "FROM meetings " +
                "WHERE sender_user_id = :userId AND status = :status " +
                "UNION " +
                "SELECT * " +
                "FROM meetings " +
                "WHERE receiver_user_id = :userId AND status = :status"
    )
    fun getMyMeetings(userId: Int, status: String): LiveData<List<Meetings>>


    @Query(
        "SELECT * FROM meetings " +
                "WHERE( sender_user_id= :senderUserId AND receiver_user_id= :receiverUserId) " +
                "OR (sender_user_id= :receiverUserId  AND receiver_user_id = :senderUserId)"
    )
    fun getMeetingDetails(senderUserId: Int, receiverUserId: Int): LiveData<Meetings>


    @Query(
        "UPDATE meetings SET status= :status " +
                "WHERE (sender_user_id= :senderUserId AND receiver_user_id= :receiverUserId AND  status = 'UPCOMING') " +
                "OR (sender_user_id= :receiverUserId AND receiver_user_id= :senderUserId AND  status = 'UPCOMING')"
    )
    suspend fun updateMeetingStatus(senderUserId: Int, receiverUserId: Int, status: String)

    @Query("UPDATE meetings SET status= :status WHERE id= :meetingId")
    suspend fun updateMeetingStatusOnId(meetingId: Int, status: String)

    @Query(
        "UPDATE meetings SET " +
                "title= :title, " +
                "date= :date, " +
                "time= :time, " +
                "place= :place " +
                "WHERE id= :meetingId"
    )
    suspend fun updateMeetingDetails(
        meetingId: Int,
        title: String,
        date: Date,
        time: String,
        place: String
    )


}