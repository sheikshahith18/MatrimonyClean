package com.example.matrimony.db.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.matrimony.db.dao.MeetingsDao
import com.example.matrimony.db.entities.Meetings
import java.util.*

class MeetingsRepository(private val meetingsDao: MeetingsDao) {

    suspend fun addNewMeeting(meetings: Meetings) {
        meetingsDao.addNewMeeting(meetings)
    }

    suspend fun deleteMeeting(senderUserId: Int, receiverUserId: Int) {
        meetingsDao.deleteMeeting(senderUserId, receiverUserId)
    }

    fun getUpcomingMeetings(userId: Int): LiveData<Meetings> {
        return meetingsDao.getUpcomingMeetings(userId)
    }

    suspend fun updateMeetingStatus(senderUserId: Int, receiverUserId: Int, status: String) {
        meetingsDao.updateMeetingStatus(senderUserId, receiverUserId, status)
    }

    suspend fun updateMeetingStatusOnId(meetingId: Int, status: String){
        meetingsDao.updateMeetingStatusOnId(meetingId, status)
    }

    suspend fun getMyMeetings(userId: Int, status: String): LiveData<List<Meetings>> {
        return meetingsDao.getMyMeetings(userId, status)
    }

    suspend fun getMeetingOnId(meetingId:Int):LiveData<Meetings>{
        return meetingsDao.getMeetingOnId(meetingId)
    }


    suspend fun getMeetingDetails(senderUserId: Int, receiverUserId: Int): LiveData<Meetings> {
        return meetingsDao.getMeetingDetails(senderUserId, receiverUserId)
    }

    suspend fun updateMeetingDetails(
        meetingId: Int,
        title: String,
        date: Date,
        time: String,
        place: String
    ) {
        meetingsDao.updateMeetingDetails(meetingId, title, date, time, place)
    }

}