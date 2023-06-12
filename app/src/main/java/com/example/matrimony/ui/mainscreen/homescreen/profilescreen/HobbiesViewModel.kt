package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.matrimony.db.entities.Hobbies
import com.example.matrimony.db.repository.HobbiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HobbiesViewModel @Inject constructor(private val hobbiesRepository: HobbiesRepository):ViewModel(){

    val selectedHobbies = sortedSetOf<String>()

    suspend fun addHobby(hobby: Hobbies){
        hobbiesRepository.addHobby(hobby)
    }


    suspend fun removeHobby(userId:Int,hobby:String){
        hobbiesRepository.removeHobby(userId, hobby)
    }

    suspend fun getHobbies(userId:Int): LiveData<List<String>> {
        return hobbiesRepository.getHobbies(userId)
    }
}