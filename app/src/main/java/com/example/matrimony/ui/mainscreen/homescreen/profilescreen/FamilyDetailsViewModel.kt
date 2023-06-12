package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.matrimony.db.entities.FamilyDetails
import com.example.matrimony.db.repository.FamilyDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FamilyDetailsViewModel @Inject constructor(private val familyDetailsRepository: FamilyDetailsRepository) :ViewModel(){

    suspend fun getFamilyDetails(userId:Int): LiveData<FamilyDetails?> {
        return familyDetailsRepository.getFamilyDetails(userId)
    }


    suspend fun setFamilyDetails(familyDetails: FamilyDetails){
        familyDetailsRepository.setFamilyDetails(familyDetails)
    }
}