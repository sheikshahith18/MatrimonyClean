package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matrimony.db.converters.ListConverter
import com.example.matrimony.db.entities.PartnerPreferences

@Dao
interface PartnerPreferenceDao {

//    @PrimaryKey
//    val id: Int,
//    val user_id: Int,
//    var age_from: Int,
//    var age_to: Int,
//    var height_from: Int,
//    var height_to: Int,
//    var marital_status: String,
//    @TypeConverters(ListConverter::class)
//    var education: List<String>,
//    @TypeConverters(ListConverter::class)
//    var employed_in: List<String>,
//    @TypeConverters(ListConverter::class)
//    var occupation: List<String>,
//    var annual_income: String,
//    var religion: String,
//    @TypeConverters(ListConverter::class)
//    var caste: List<String>,
//    @TypeConverters(ListConverter::class)
//    var star: List<String>,
//    @TypeConverters(ListConverter::class)
//    var zodiac: List<String>,
//    var state: String,
//    @TypeConverters(ListConverter::class)
//    var city: List<String>


    @Query("Select * FROM partner_preferences WHERE user_id= :userId")
    fun getPartnerPreference(userId: Int): LiveData<PartnerPreferences>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPreference(partnerPreferences: PartnerPreferences)

    @Query("DELETE FROM partner_preferences WHERE user_id= :userId")
    suspend fun clearPreference(userId: Int)

    @Query("UPDATE partner_preferences SET " +
            "age_from = :ageFrom," +
            "age_to= :ageTo," +
            "height_from= :heightFrom," +
            "height_to= :heightTo," +
            "marital_status= :maritalStatus," +
            "education= :education," +
            "employed_in= :employedIn," +
            "occupation= :occupation," +
            "religion= :religion," +
            "caste= :caste," +
            "star= :star," +
            "zodiac= :zodiac," +
            "state= :state," +
            "city= :city " +
            "WHERE user_id= :userId")
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
    )



    @Query("SELECT EXISTS (SELECT user_id FROM partner_preferences WHERE user_id= :userId)")
    suspend fun isPreferenceSet(userId:Int):Boolean

}