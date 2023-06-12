package com.example.matrimony

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.db.entities.User
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

const val TAG = "Matrimony"


@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {

    private val userProfileViewModel by viewModels<UserProfileViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_layout)


        lifecycleScope.launch {

            Log.i(TAG, userProfileViewModel.getUserData(16).toString())
            Log.i(TAG, userProfileViewModel.getUserMobile(1).value.toString())
//            Log.i(TAG, userProfileViewModel.getFilteredUserData1(
//                ).value.toString())
        }


    }
}