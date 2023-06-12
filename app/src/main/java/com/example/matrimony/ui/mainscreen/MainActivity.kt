package com.example.matrimony.ui.mainscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.InjectDataViewModel
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityMainBinding
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsPageFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.homescreen.HomePageFragment
import com.example.matrimony.ui.mainscreen.meetingscreen.MeetingsPageFragment
import com.example.matrimony.ui.mainscreen.searchscreen.SearchPageFragment
import com.example.matrimony.ui.mainscreen.shortlistsscreen_clean.presentation.view.ShortlistsPageFragment
import com.example.matrimony.utils.CURRENT_USER_GENDER
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val SELECTED_ITEM = "SELECTED_ITEM_ID"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentContainer: FrameLayout
    lateinit var bottomNavigationView: BottomNavigationView

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val connectionsViewModel by viewModels<ConnectionsViewModel>()
    private val injectDataViewModel by viewModels<InjectDataViewModel>()

    private var selectedItemId = 0

    private val homePageFragment = HomePageFragment()
    private val searchPageFragment = SearchPageFragment()
    private val shortlistsPageFragment = ShortlistsPageFragment()
    private val connectionsPageFragment = ConnectionsPageFragment()
    private val meetingsPageFragment = MeetingsPageFragment()

//    var loaded=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        if()

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return

        lifecycleScope.launch {

            val loaded = sharedPref.getInt("DATA_LOADED", -1)
//            Toast.makeText(this@MainActivity, "loaded=$loaded",Toast.LENGTH_SHORT).show()
//            userProfileViewModel.loaded = false
            if (loaded == -1) {
                binding.layoutLoading.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.GONE
                injectDataViewModel.context = application

                while (injectDataViewModel.count > userProfileViewModel.getNoOfUsers()) {
                    Log.i(TAG, "inject count=${injectDataViewModel.count}, noOfUsers=${userProfileViewModel.getNoOfUsers()}")
                }
                Log.i(TAG, "MainAct Loaded")
                val editor = sharedPref.edit()
                editor.putInt("DATA_LOADED", 1)
//                userProfileViewModel.loaded=true
//                loaded = 1
                editor.apply()

                init()
            } else {
                binding.layoutLoading.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
            }

            Log.i(TAG,"Completed in main act")
//            binding.bottomNavView.selectedItemId = R.id.nav_home
            Log.i(TAG,"userproviewmodel loaded=${userProfileViewModel.loaded}")

            setBottomNavView()
            if (savedInstanceState == null || loaded==-1) {
                Log.i(TAG,"Before Main Init")
//                loaded=1
                init()
//                userProfileViewModel.loaded = true
            }
//            if (loaded == 1) {
//                userProfileViewModel.loaded = true
                binding.layoutLoading.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
//            }



            fragmentContainer = binding.fragmentContainer

//            if(savedInstanceState!=null){
//                val selectedItem = savedInstanceState.getInt(SELECTED_ITEM)
//                bottomNavigationView.selectedItemId = selectedItem
//            }

//        setBottomNavView()

//            setBottomNavView()
//            binding.bottomNavView.selectedItemId = R.id.nav_home
        }

        Log.i(TAG,"Main onCreate after lifecycle scope")
//        if(userProfileViewModel.currentNavItem==R.id.nav_home)
//        setCurrentFragment(HomePageFragment())
    }

    private fun init() {
        Log.i(TAG,"Inside Main Init")
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        lifecycleScope.launch {
//            if (userId != -1) {

            val gender = userProfileViewModel.getUserGender(userId)
            Log.i(TAG, "Inside MainActivity currentUserId=$userId")
            Log.i(TAG, "Inside MainActivity currentGender=$gender")
            val editor = sharedPref.edit()
            editor.putString(CURRENT_USER_GENDER, gender)
            editor.apply()

            if (connectionsViewModel.isRequestsPending(userId)) {
                if (userProfileViewModel.initialLogin) {
                    bottomNavigationView.getOrCreateBadge(R.id.nav_connections).apply {
                        isVisible = true
                    }
                    userProfileViewModel.initialLogin = false
                }
            }

            binding.bottomNavView.selectedItemId = R.id.nav_home
        }

    }



    private var previousItem: Int? = null
    private var start = true

    private fun setBottomNavView() {
        bottomNavigationView = binding.bottomNavView
//        bottomNavigationView.selectedItemId=R.id.nav_home

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId != R.id.nav_search) {
                val sharedPref =
                    getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                if (sharedPref.getBoolean("PREF_FILTER_APPLIED", false)
                    || sharedPref.getBoolean("BASED_ON_CITY_APPLIED", false)
                    || sharedPref.getBoolean("BASED_ON_EDUCATION_APPLIED", false)
                    || sharedPref.getBoolean("BASED_ON_OCCUPATION_APPLIED", false)
                ) {
                    val editor = sharedPref.edit()

                    editor.remove("PREF_FILTER_APPLIED")
                    editor.remove("BASED_ON_CITY_APPLIED")
                    editor.remove("BASED_ON_EDUCATION_APPLIED")
                    editor.remove("BASED_ON_OCCUPATION_APPLIED")
                    editor.putString("FILTER_STATUS", "not_applied")
                    editor.putBoolean("FILTER_CLEARED", true)
                    editor.apply()
                    clearFilters()
                }
            }

            when (menuItem.itemId) {
                R.id.nav_home -> {
                    if (previousItem != R.id.nav_home) {
                        previousItem = R.id.nav_home
                        supportFragmentManager.beginTransaction().apply {

                            if (start) {
                                start = false
                                replace(R.id.fragment_container, homePageFragment, "nav_home")
                            } else {
                                addToBackStack("HOME_FRAGMENT")
                                replace(R.id.fragment_container, homePageFragment, "nav_home")

                            }
                            commit()

                        }
                    } else {
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.fragment_container)
                        while (true) {
                            if (fragment != null && fragment is HomePageFragment) {
                                break
                            } else {
                                supportFragmentManager.popBackStackImmediate()
                                fragment =
                                    supportFragmentManager.findFragmentById(R.id.fragment_container)
                            }
                        }

                    }
                }
                R.id.nav_search -> {
                    if (previousItem != R.id.nav_search) {
                        Log.i(TAG+1,"Previous Item != nav_search")
                        previousItem = R.id.nav_search
                        supportFragmentManager.beginTransaction().apply {
                            addToBackStack("nav_search")
                            replace(R.id.fragment_container, searchPageFragment, "nav_search")
                            commit()
                        }
                    } else {
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.fragment_container)
                        while (true) {
                            if (fragment != null && fragment is SearchPageFragment) {
                                break
                            } else {
                                supportFragmentManager.popBackStackImmediate()
                                fragment =
                                    supportFragmentManager.findFragmentById(R.id.fragment_container)
                            }
                        }

                    }

                }
                R.id.nav_shortlists -> {
                    if (previousItem != R.id.nav_shortlists) {
                        previousItem = R.id.nav_shortlists
                        supportFragmentManager.beginTransaction().apply {
                            addToBackStack("nav_shortlists")
                            replace(
                                R.id.fragment_container,
                                shortlistsPageFragment,
                                "nav_shortlists"
                            )
                            commit()
                        }
                    } else {
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.fragment_container)
                        while (true) {
                            if (fragment != null && fragment is ShortlistsPageFragment) {
                                break
                            } else {
                                supportFragmentManager.popBackStackImmediate()
                                fragment =
                                    supportFragmentManager.findFragmentById(R.id.fragment_container)
                            }
                        }

                    }
                }

                R.id.nav_connections -> {
                    if (previousItem != R.id.nav_connections) {
                        previousItem = R.id.nav_connections
                        supportFragmentManager.beginTransaction().apply {
                            addToBackStack("nav_connections")
                            replace(
                                R.id.fragment_container,
                                connectionsPageFragment,
                                "nav_connections"
                            )
                            commit()
                        }
                    } else {
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.fragment_container)
                        while (true) {
                            if (fragment != null && fragment is ConnectionsPageFragment) {
                                break
                            } else {
                                supportFragmentManager.popBackStackImmediate()
                                fragment =
                                    supportFragmentManager.findFragmentById(R.id.fragment_container)
                            }
                        }

                    }

                }

                R.id.nav_meetings -> {
                    if (previousItem != R.id.nav_meetings) {
                        previousItem = R.id.nav_meetings
                        supportFragmentManager.beginTransaction().apply {
                            addToBackStack("nav_meetings")
                            replace(
                                R.id.fragment_container,
                                meetingsPageFragment,
                                "nav_meetings"
                            )
                            commit()
                        }
                    } else {
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.fragment_container)
                        while (true) {
                            if (fragment != null && fragment is MeetingsPageFragment) {
                                break
                            } else {
                                supportFragmentManager.popBackStackImmediate()
                                fragment =
                                    supportFragmentManager.findFragmentById(R.id.fragment_container)
                            }
                        }
                    }
                }
            }

            if (menuItem.itemId == R.id.nav_connections) {
                bottomNavigationView.getOrCreateBadge(R.id.nav_connections).apply {
                    isVisible = false
                }
            }
//            setCurrentMenuItem(menuItem.itemId)
            true
        }
    }




    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }



    override fun onBackPressed() {
//        if (!bottomNavigationView.menu.getItem(0).isChecked) {
//            bottomNavigationView.menu.getItem(0).isChecked = true
//            setCurrentFragment(homePagFragment)
//        } else
//            super.onBackPressed()

        val currentFragment=supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment is SearchPageFragment){
            val searchText=currentFragment.view?.findViewById<EditText>(R.id.et_search)
            if(searchText?.hasFocus()==true){
                searchText.clearFocus()
                searchText.setText("")
                return
            }
        }

        val homeFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) is HomePageFragment

        if (!homeFragment) {
            // navigate to home fragment
            bottomNavigationView.selectedItemId = R.id.nav_home
        } else {
            // exit the activity
            finish()
        }
    }


    fun navigateBottomView(viewId: Int) {
        Log.i(TAG, "navigate")

        when (viewId) {
            R.id.menu_shortlisted_profiles -> {
                binding.bottomNavView.selectedItemId = R.id.nav_shortlists
                setCurrentFragment(shortlistsPageFragment)
//                binding.bottomNavView.selectedItemId = R.id.menu_shortlisted_profiles
            }
            R.id.menu_connections -> {
                binding.bottomNavView.selectedItemId = R.id.nav_connections
                setCurrentFragment(connectionsPageFragment)
//                binding.bottomNavView.selectedItemId = R.id.menu_shortlisted_profiles
            }
            R.id.menu_meetings -> {
                binding.bottomNavView.selectedItemId = R.id.nav_meetings
                setCurrentFragment(meetingsPageFragment)
//                binding.bottomNavView.selectedItemId = R.id.menu_meetings
            }
        }
//        binding.bottomNavView.post {
//            binding.bottomNavView.setOnItemSelectedListener(null)
//            setBottomNavView()
//        }
    }

    fun inflateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }



    private fun clearFilters() {
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val previousFilterStatus = sharedPref.getString("FILTER_STATUS", "not_applied")
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
//        Log.i(TAG, "clearPref filterStat ${sharedPref.getString("FILTER_STATUS", "null")}")
    }
}