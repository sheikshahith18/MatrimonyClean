package com.example.matrimony.ui.mainscreen.searchscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matrimony.db.repository.UserRepository
import com.example.matrimony.models.SortOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    var sortOptions=SortOptions.DATE_ASC

    val sortChange=MutableLiveData<Boolean>(false)
    val filterChange=MutableLiveData<Boolean>(false)

    var nameSortFlag = -1
    var ageSortFlag = -1
    var idSortFlag = 1

    var ascSortFlag = 1
    var descSortFlag = -1

    var selectedTab=0

    var loaded = false

    val selectedEducations = sortedSetOf<String>()
    val selectedEmployedIns = sortedSetOf<String>()
    val selectedOccupation = sortedSetOf<String>()
    val selectedCastes = sortedSetOf<String>()
    val selectedStars = sortedSetOf<String>()
    val selectedZodiacs = sortedSetOf<String>()
    val selectedCities = sortedSetOf<String>()
    val selectedMaritalStatus = sortedSetOf<String>()

    var selectedState = ""
    var selectedReligion = ""



}