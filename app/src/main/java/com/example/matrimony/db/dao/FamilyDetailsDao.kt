package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matrimony.db.entities.FamilyDetails


@Dao
interface FamilyDetailsDao {
//    @PrimaryKey
//    val user_id:Int,
//    var fathers_name:String,
//    var mothers_name:String,
//    var no_of_brothers:Int,
//    var married_brothers:Int?,
//    var no_of_sisters:Int,
//    var married_sisters:Int


    @Query("SELECT * FROM family_details WHERE user_id= :userId")
    fun getFamilyDetails(userId:Int):LiveData<FamilyDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFamilyDetails(familyDetails: FamilyDetails)
}