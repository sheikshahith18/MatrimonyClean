package com.example.matrimony.ui.mainscreen.meetingscreen

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.Meetings
import com.example.matrimony.db.repository.MeetingsRepository
import com.example.matrimony.db.repository.UserRepository
import com.example.matrimony.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MeetingsViewModel @Inject constructor(
    private val meetingsRepository: MeetingsRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

    var userId = -1
    var currentSelectedPage = 0

    suspend fun getProfilePic(userId: Int): LiveData<Bitmap?> {
        return userRepository.getProfilePic(userId)
    }

    suspend fun addNewMeeting(meetings: Meetings) {
        meetingsRepository.addNewMeeting(meetings)
    }

    suspend fun deleteMeeting(senderUserId: Int, receiverUserId: Int) {
        meetingsRepository.deleteMeeting(senderUserId, receiverUserId)
    }

    fun getUpcomingMeetings(userId: Int): LiveData<Meetings> {
        return meetingsRepository.getUpcomingMeetings(userId)
    }


    suspend fun getMeetingOnId(meetingId:Int):LiveData<Meetings>{
        return meetingsRepository.getMeetingOnId(meetingId)
    }

    fun updateMeetingStatus(senderUserId: Int, receiverUserId: Int, status: String) {
        viewModelScope.launch {
            meetingsRepository.updateMeetingStatus(senderUserId, receiverUserId, status)
        }
    }

    fun updateMeetingStatusOnId(meetingId: Int, status: String){
        viewModelScope.launch {
            meetingsRepository.updateMeetingStatusOnId(meetingId, status)
        }
    }

    suspend fun getMyMeetings(userId: Int, status: String): LiveData<List<Meetings>> {
        return meetingsRepository.getMyMeetings(userId, status)
    }


    suspend fun getMeetingDetails(senderUserId: Int, receiverUserId: Int): LiveData<Meetings> {
        return meetingsRepository.getMeetingDetails(senderUserId, receiverUserId)
    }

    suspend fun getUserData(userId: Int): UserData {
        return userRepository.getUserData(userId)
    }

    suspend fun updateMeetingDetails(
        meetingId: Int,
        title: String,
        date: Date,
        time: String,
        place: String
    ) {
        meetingsRepository.updateMeetingDetails(meetingId, title, date, time, place)
    }


}