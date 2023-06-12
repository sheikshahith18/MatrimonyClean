package com.example.matrimony.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.InjectDataViewModel
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.ui.loginscreen.LoginViewModel
import com.example.matrimony.ui.loginscreen.SignInActivity
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    private val injectDataViewModel by viewModels<InjectDataViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return
        val loaded = sharedPref.getInt("DATA_LOADED", -1)
        Log.i(TAG, "Loaded : $loaded")


        clearFilters()
        val editor = sharedPref.edit()

        if (injectDataViewModel.initial) {
            injectDataViewModel.context = application

            GlobalScope.launch() {
                if (loaded == -1) {
                    deleteDatabase("app_database")
                    Log.i(TAG, "Profile Loading")
                    injectDataViewModel.addAccount()
                    injectDataViewModel.addHabits()
                    injectDataViewModel.addFamilyDetails()
                    injectDataViewModel.addUsers()
                } else {
                    Log.i(TAG, "Profile AlreadyLoaded")
                }

                editor.putInt("DATA_LOADED", 1)
//            editor.apply()
//            editor.putInt(CURRENT_USER_ID, -1)
//            editor.putString(CURRENT_USER_GENDER, "")
                editor.apply()
//            clearPreferences()


//            val editor = sharedPref.edit()
            }
        }
        val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
        Log.i(TAG, "Splash CurrUSerId $userId")
        if (userId == -1) {
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun clearFilters() {
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        Log.i(TAG, "service clear preference")
        val editor = sharedPref.edit()
        editor.remove("AGE_FROM_FILTER")
        editor.remove("AGE_TO_FILTER")
        editor.remove("HEIGHT_FROM_FILTER")
        editor.remove("HEIGHT_TO_FILTER")
        editor.remove("MARITAL_STATUS_FILTER")
        editor.remove("EDUCATION_FILTER")
        editor.remove("EMPLOYED_IN_FILTER")
        editor.remove("OCCUPATION_FILTER")
        editor.remove("ANNUAL_INCOME_FILTER")
        editor.remove("RELIGION_FILTER")
        editor.remove("CASTE_FILTER")
        editor.remove("STAR_FILTER")
        editor.remove("ZODIAC_FILTER")
        editor.remove("STATE_FILTER")
        editor.remove("CITY_FILTER")
        editor.putString("FILTER_STATUS", "not_applied")
        editor.apply()

        Log.i(TAG, "clearPref filterStat ${sharedPref.getString("FILTER_STATUS", "null")}")
    }

}