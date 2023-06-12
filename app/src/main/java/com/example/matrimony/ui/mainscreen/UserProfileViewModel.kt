package com.example.matrimony.ui.mainscreen

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.db.entities.*
import com.example.matrimony.db.repository.*
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val shortlistsRepository: ShortlistsRepository,
    private val connectionsRepository: ConnectionsRepository,
    private val hobbiesRepository: HobbiesRepository,
    private val familyDetailsRepository: FamilyDetailsRepository
) : ViewModel() {



    var sendConnection: Boolean? = null
    var initialLogin=true

    var preferredUserIds= mutableListOf<Int>()


    var currentNavMenu:Int?=null
    var currentSwipeImage=0

    var gender = ""
    var userId = -1
    var isCurrentUser = false
    var currentUserId = -1
    var isUserConnected = false

    var dialogLoad = false
    var dialogUserId = -1
    var dialogUserName = ""
    var dialogAdapterPosition = -1

    var loaded = true

    var city:String?=""
    var state=""
    var education=""
    var occupation=""

    var profilesLoaded=false
    var searchProfilesLoaded=false



    var currentUserData = MutableLiveData<UserData>()

    var currentNavItem = R.id.nav_home

    var dialog:RemoveConnectionDialogFragment = RemoveConnectionDialogFragment()

    suspend fun getUser(userId: Int): LiveData<User> {
        return userRepository.getUser(userId)
    }

    suspend fun getNoOfUsers():Int{
        return userRepository.getNoOfUsers()
    }


    suspend fun getNameOfUser(userId: Int): LiveData<String> {
        return userRepository.getNameOfUser(userId)
    }

    fun updateProfilePic(userId: Int, image: Bitmap?) {
        CoroutineScope(Dispatchers.Main).launch {
            userRepository.updateProfilePic(userId, image)
        }
    }

    suspend fun getProfilePic(userId: Int): LiveData<Bitmap?> {
        return userRepository.getProfilePic(userId)
    }

    fun addConnection(connection: Connections) {
        viewModelScope.launch {
            connectionsRepository.addConnection(connection)
        }
    }

    fun removeConnection(userId: Int, connectedUserId: Int) {
        viewModelScope.launch {
            connectionsRepository.removeConnection(userId, connectedUserId)
        }
    }


    fun shortlistUser(shortlist: Shortlists) {
        viewModelScope.launch {
            shortlistsRepository.addShortlist(shortlist)
        }
    }

    fun removeShortlist(userId: Int, shortlistedUserId: Int) {
        Log.i(TAG, "removeShortlist")
        viewModelScope.launch {
            shortlistsRepository.removeShortlist(userId, shortlistedUserId)
        }
    }


    suspend fun getUserData(userId: Int): UserData {
        return userRepository.getUserData(userId)
    }

    suspend fun getShortlistedStatus(shortlistedUserId: Int): LiveData<Boolean> {
        return shortlistsRepository.getShortlistedStatus(this.userId, shortlistedUserId)
    }

    fun removeShortlist(shortlistedUserId: Int) {
        viewModelScope.launch {
            shortlistsRepository.removeShortlist(
                this@UserProfileViewModel.userId,
                shortlistedUserId
            )
        }
    }

    suspend fun isConnectionAvailable(connectedUserId: Int): Boolean {
        return connectionsRepository.isConnectionAvailable(this.userId, connectedUserId)
    }

    suspend fun getConnectionStatus(connectedUserId: Int): LiveData<String?> {
        return connectionsRepository.getConnectionStatus(this.userId, connectedUserId)
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }

    suspend fun getUserGender(userId: Int): String {
        return userRepository.getUserGender(userId)
    }

    suspend fun getUsersBasedOnGender(gender: String): LiveData<List<UserData>> {
        return userRepository.getUsersBasedOnGender(gender)
    }


    suspend fun updateUser(
        userId: Int,
        name: String,
        dob: Date,
        religion: String,
        maritalStatus: String,
        noOfChildren: Int?,
        caste: String?,
        zodiac: String,
        star: String,
        state: String,
        city: String?,
        height: String,
        physicalStatus: String,
        education: String,
        employedIn: String,
        occupation: String,
        annualIncome: String,
        familyStatus: String,
        familyType: String,
        about: String
    ) {
        userRepository.updateUser(
            userId,
            name,
            dob,
            religion,
            maritalStatus,
            noOfChildren,
            caste,
            zodiac,
            star,
            state,
            city,
            height,
            physicalStatus,
            education,
            employedIn,
            occupation,
            annualIncome,
            familyStatus,
            familyType,
            about
        )
    }

    suspend fun getFilteredUserData(
        gender: String,
        ageFrom: Int,
        ageTo: Int,
        heightArraySize: Int,
        heightArray: List<String>,
        maritalStatusSize: Int,
        maritalStatusArray: List<String>,
        educationArraySize: Int,
        educationArray: List<String>,
        employedInSize: Int,
        employedInArray: List<String>,
        occupationArraySize: Int,
        occupationArray: List<String>,
        religionArraySize: Int,
        religionArray: List<String>,
        casteArraySize: Int,
        casteArray: List<String>,
        starArraySize: Int,
        starArray: List<String>,
        zodiacArraySize: Int,
        zodiacArray: List<String>,
        stateArraySize: Int,
        stateArray: List<String>,
        cityArraySize: Int,
        cityArray: List<String>,
        nameAscFlag: Int,
        nameDescFlag: Int,
        ageAscFlag: Int,
        ageDescFlag: Int,
        idAscFlag: Int,
        idDescFlag: Int,
        searchText: String
    ): LiveData<List<UserData>> {
        return userRepository.getFilteredUserData(
            gender,
            ageFrom,
            ageTo,
            heightArraySize,
            heightArray,
            maritalStatusSize,
            maritalStatusArray,
            educationArraySize,
            educationArray,
            employedInSize,
            employedInArray,
            occupationArraySize,
            occupationArray,
            religionArraySize,
            religionArray,
            casteArraySize,
            casteArray,
            starArraySize,
            starArray,
            zodiacArraySize,
            zodiacArray,
            stateArraySize,
            stateArray,
            cityArraySize,
            cityArray,
            nameAscFlag,
            nameDescFlag,
            ageAscFlag,
            ageDescFlag,
            idAscFlag,
            idDescFlag,
            searchText
        )
    }



    suspend fun getUserMobile(userId: Int): LiveData<String> {
        return userRepository.getUserMobile(userId)
    }


    suspend fun updateMobile(userId: Int, newMobile: String) {
        userRepository.updateMobile(userId, newMobile)
    }

}