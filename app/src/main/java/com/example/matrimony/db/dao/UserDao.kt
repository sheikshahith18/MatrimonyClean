package com.example.matrimony.db.dao

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.User
import com.example.matrimony.models.UserData
import java.util.*

@Dao
interface UserDao {


    //    @Query("SELECT * FROM user")
//    @Query("SELECT * FROM user WHERE gender= 'M'")
//    @Query("SELECT * FROM user WHERE gender= 'F'")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)


    @Query("SELECT COUNT(user_id) FROM user")
    suspend fun getNoOfUsers():Int

    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUser(userId: Int): LiveData<User>

    @Query("SELECT * FROM user WHERE user_id= :userId ORDER BY :orderBy")
    fun getSortedUser(userId:Int,orderBy:String):User

    @Query("DELETE FROM user WHERE user_id= :userId")
    suspend fun deleteUser(userId:Int)

    @Query(
        "UPDATE user " +
                "SET " +
                "name= :name," +
                "dob= :dob," +
                "religion= :religion," +
                "marital_status= :maritalStatus," +
                "no_of_children= :noOfChildren," +
                "caste= :caste," +
                "zodiac= :zodiac," +
                "star= :star," +
                "state= :state," +
                "city= :city," +
                "height= :height," +
                "physical_status= :physicalStatus," +
                "education= :education," +
                "employed_in= :employedIn," +
                "occupation= :occupation," +
                "annual_income= :annualIncome," +
                "family_status= :familyStatus," +
                "family_type= :familyType," +
                "about= :about " +
                "WHERE user_id= :userId"
    )
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
    )


    @Query("SELECT user_id as userId,name,dob,religion,caste,state,city,height,profile_pic,education,occupation FROM user WHERE user_id = :userId")
    suspend fun getUserData(userId: Int): UserData

    @Query("UPDATE user SET profile_pic= :image WHERE user_id = :userId")
    suspend fun updateProfilePic(userId: Int, image: Bitmap?)

    @Query("SELECT profile_pic FROM user  WHERE user_id = :userId")
    fun getProfilePic(userId: Int):LiveData<Bitmap?>

    @Query("SELECT user_id as userId,name,dob,religion,caste,state,city,height,profile_pic,education,occupation FROM user WHERE user_id IN (:userIdList)")
    fun getUsersListOnUserId(userIdList: List<Int>): LiveData<List<UserData>>

    @Query("SELECT name FROM user WHERE user_id= :userId")
    fun getNameOfUser(userId: Int): LiveData<String>

//    fun getAllUsers(): LiveData<List<UserData>>

//    fun getAllMaleUsers(): LiveData<List<UserData>>

//    fun getAllFemaleUsers(): LiveData<List<UserData>>




    @Query("SELECT user_id as userId,name,dob,religion,caste,state,city,height,profile_pic,education,occupation FROM user WHERE gender= :gender")
    fun getUsersBasedOnGender(gender: String): LiveData<List<UserData>>


    @Query("SELECT gender FROM user WHERE user_id= :userId")
    suspend fun getUserGender(userId: Int): String


    @Query(
        "UPDATE user " +
                "SET " +
                "height = :height, " +
                "physical_status = :physicalStatus, " +
                "marital_status = :maritalStatus, " +
                "no_of_children = :childrenCount, " +
                "education = :education, " +
                "employed_in = :employedIn, " +
                "occupation = :occupation, " +
                "annual_income = :annualIncome, " +
                "family_status = :familyStatus, " +
                "family_type = :familyType, " +
                "about = :about " +
                "WHERE " +
                "user_id = :userId"
    )
    suspend fun updateAdditionalDetails(
        userId: Int,
        height: String,
        physicalStatus: String,
        maritalStatus: String,
        childrenCount: Int?,
        education: String,
        employedIn: String,
        occupation: String,
        annualIncome: String,
        familyStatus: String,
        familyType: String,
        about: String
    )


    @Query(
        "SELECT user_id as userId,name,dob,religion,caste,state,city,height,profile_pic,education,occupation " +
                "FROM user " +
                "WHERE " +
                "gender = :gender " +
                "AND (age BETWEEN :ageFrom AND :ageTo) " +
                "AND (:heightArraySize = 0 OR height IN (:heightArray)  OR height = 'Not Set') " +
                "AND (:maritalStatusSize = 0 OR marital_status IN (:maritalStatusArray)) " +
                "AND (:educationArraySize = 0 OR education IN (:educationArray)) " +
                "AND (:employedInArraySize = 0 OR employed_in IN (:employedInArray)) " +
                "AND (:occupationArraySize = 0 OR occupation IN (:occupationArray)) " +
                "AND (:religionArraySize = 0 OR religion IN (:religionArray)) " +
                "AND (:casteArraySize = 0 OR caste IN (:casteArray)) " +
                "AND (:starArraySize = 0 OR star IN (:starArray)) " +
                "AND (:zodiacArraySize = 0 OR zodiac IN (:zodiacArray)) " +
                "AND (:stateArraySize = 0 OR state IN (:stateArray)) " +
                "AND (:cityArraySize = 0 OR city IN (:cityArray)) " +
                "AND name LIKE '%'  || :searchText || '%' " +
                "ORDER BY " +
                "CASE WHEN 1= :nameAscFlag THEN name END ASC, " +
                "CASE WHEN 1= :nameDescFlag THEN name END DESC, " +
                "CASE WHEN 1= :ageAscFlag THEN age END ASC, " +
                "CASE WHEN 1= :ageDescFlag THEN age END DESC, " +
                "CASE WHEN 1= :idAscFlag THEN user_id END ASC," +
                "CASE WHEN 1= :idDescFlag THEN user_id END DESC"
    )
    fun getFilteredUserData(
        gender: String,
        ageFrom: Int,
        ageTo: Int,
        heightArraySize: Int,
        heightArray: List<String>,
        maritalStatusSize: Int,
        maritalStatusArray: List<String>,
        educationArraySize: Int,
        educationArray: List<String>,
        employedInArraySize: Int,
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
        nameAscFlag:Int,
        nameDescFlag:Int,
        ageAscFlag:Int,
        ageDescFlag:Int,
        idAscFlag:Int,
        idDescFlag:Int,
        searchText:String
    ): LiveData<List<UserData>>



}
