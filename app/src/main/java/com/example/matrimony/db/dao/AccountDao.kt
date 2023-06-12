package com.example.matrimony.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matrimony.db.entities.Account


@Dao
interface AccountDao {

    @Query("SELECT password FROM account WHERE user_id = :userId")
    suspend fun getPassword(userId:Int):String
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: Account)

    @Query("SELECT user_id FROM account WHERE email= :email")
    suspend fun getUserByEmail(email: String): Int

    @Query("SELECT user_id FROM account WHERE mobile_no = :mobile")
    suspend fun getUserByMobile(mobile: String): Int

    @Query("SELECT user_id FROM account WHERE mobile_no = :info OR email = :info")
    suspend fun getUserByEmailOrMobile(info:String):Int

    @Query("SELECT mobile_no FROM account WHERE user_id= :userId")
     fun getUserMobile(userId:Int):LiveData<String>


    @Query("DELETE FROM account WHERE user_id= :userId")
    suspend fun deleteAccount(userId: Int)

    @Query("SELECT * FROM account")
    fun getAllAccounts(): LiveData<List<Account>>


    @Query("SELECT EXISTS(SELECT * FROM account WHERE email = :email)")
    suspend fun isEmailExist(email: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE mobile_no = :mobile)")
    suspend fun isMobileExist(mobile: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE email= :email AND password= :password)")
    suspend fun isEmailAndPasswordMatched(email: String, password: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE mobile_no= :mobile AND password= :password)")
    suspend fun isMobileAndPasswordMatched(mobile: String, password: String): Boolean


    @Query("SELECT EXISTS(SELECT * FROM account WHERE (email = :info OR mobile_no = :info) AND password = :password)")
    suspend fun isCredentialsMatched(info: String, password: String): Boolean


    @Query("SELECT EXISTS (SELECT * FROM account WHERE user_id= :userId AND password= :password)")
    suspend fun isPasswordValid(userId:Int,password:String):Boolean
    @Query("UPDATE account SET password = :newPassword WHERE user_id = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)

    @Query("UPDATE account SET mobile_no = :newMobile WHERE user_id = :userId")
    suspend fun updateMobile(userId: Int, newMobile: String)
}