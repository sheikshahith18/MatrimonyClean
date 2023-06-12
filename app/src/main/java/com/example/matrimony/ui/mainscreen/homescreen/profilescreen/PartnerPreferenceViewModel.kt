package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.PartnerPreferences
import com.example.matrimony.db.repository.PartnerPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PartnerPreferenceViewModel @Inject constructor(private val partnerPreferenceRepository: PartnerPreferenceRepository) :
    ViewModel() {

    val selectedEducations = sortedSetOf<String>()
    val selectedEmployedIns = sortedSetOf<String>()
    val selectedOccupation = sortedSetOf<String>()
    val selectedCastes = sortedSetOf<String>()
    val selectedStars = sortedSetOf<String>()
    val selectedZodiacs = sortedSetOf<String>()
    val selectedCities = sortedSetOf<String>()
    val selectedMaritalStatus = sortedSetOf<String>()

    var loaded = false

    suspend fun getPartnerPreference(userId: Int): LiveData<PartnerPreferences> {
        return partnerPreferenceRepository.getPartnerPreference(userId)
    }

    fun clearPreference(userId: Int) {
        viewModelScope.launch {
            partnerPreferenceRepository.clearPreference(userId)
        }
    }

    fun addPreference(partnerPreferences: PartnerPreferences) {
        viewModelScope.launch {
            partnerPreferenceRepository.addPreference(partnerPreferences)
        }
    }


    suspend fun updatePartnerPreference(
        userId: Int,
        ageFrom: Int,
        ageTo: Int,
        heightFrom: String,
        heightTo: String,
        maritalStatus: String,
        education: List<String>,
        employedIn: List<String>,
        occupation: List<String>,
        religion: String,
        caste: List<String>,
        star: List<String>,
        zodiac: List<String>,
        state: String,
        city: List<String>
    ) {
        partnerPreferenceRepository.updatePartnerPreference(
            userId,
            ageFrom,
            ageTo,
            heightFrom,
            heightTo,
            maritalStatus,
            education,
            employedIn,
            occupation,
            religion,
            caste,
            star,
            zodiac,
            state,
            city
        )
    }

    suspend fun isPreferenceSet(userId: Int): Boolean {
        return partnerPreferenceRepository.isPreferenceSet(userId)
    }

}