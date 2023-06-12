package com.example.matrimony.ui.mainscreen.homescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.paging.util.getOffset
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.AutoImageSwipeAdapter
import com.example.matrimony.adapters.ProfilesGridAdapter
import com.example.matrimony.databinding.FragmentHomePageBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.loginscreen.SignInActivity

import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.PartnerPreferenceViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsActivity
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.utils.CURRENT_USER_GENDER
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomePageFragment : Fragment() {

    lateinit var binding: FragmentHomePageBinding
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val settingsViewModel by activityViewModels<SettingsViewModel>()
    private val partnerPreferenceViewModel by activityViewModels<PartnerPreferenceViewModel>()

    private val albumViewModel by activityViewModels<AlbumViewModel>()

    private var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        Log.i(TAG, "HomeFragment fragmentView = $fragmentView")
        if (fragmentView == null) {

            binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)
            fragmentView = binding.root

            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
            userProfileViewModel.userId = userId
            userProfileViewModel.gender = sharedPref.getString(CURRENT_USER_GENDER, null)
                ?: throw java.lang.Exception("user gender null")
            Log.i(TAG, "current userId $userId")
            Log.i(TAG, "current user gender ${userProfileViewModel.gender}")

            binding.tvSeeAllPrefProfiles.setOnClickListener {
                lifecycleScope.launch {
                    partnerPreferenceViewModel.getPartnerPreference(userProfileViewModel.userId)
                        .observe(viewLifecycleOwner) { preferences ->
                            if (preferences == null) {
                                (requireActivity() as MainActivity).apply {
                                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId =
                                        R.id.nav_search
                                }
                                return@observe
                            }

                            clearFilters()
                            val editor = sharedPref.edit()
                            editor.putString("FILTER_STATUS", "applied")
                            editor.putInt("AGE_FROM_FILTER", preferences.age_from)
                            editor.putInt("AGE_TO_FILTER", preferences.age_to)
                            editor.putString("HEIGHT_FROM_FILTER", preferences.height_from)
                            editor.putString("HEIGHT_TO_FILTER", preferences.height_to)
                            editor.putStringSet(
                                "MARITAL_STATUS_FILTER",
                                preferences.marital_status?.toSet() ?: emptySet()
                            )
                            editor.putStringSet(
                                "EDUCATION_FILTER",
                                preferences.education?.toSet() ?: emptySet()
                            )
                            editor.putStringSet(
                                "EMPLOYED_IN_FILTER",
                                preferences.employed_in?.toSet() ?: emptySet()
                            )
                            editor.putStringSet(
                                "OCCUPATION_FILTER",
                                preferences.occupation?.toSet() ?: emptySet()
                            )
//                            editor.putString(
//                                "ANNUAL_INCOME_FILTER",
//                                preferences.annual_income ?: ""
//                            )
                            editor.putString("RELIGION_FILTER", preferences.religion ?: "")
                            editor.putStringSet(
                                "CASTE_FILTER",
                                preferences.caste?.toSet() ?: emptySet()
                            )
                            editor.putStringSet(
                                "STAR_FILTER",
                                preferences.star?.toSet() ?: emptySet()
                            )
                            editor.putStringSet(
                                "ZODIAC_FILTER",
                                preferences.zodiac?.toSet() ?: emptySet()
                            )
                            editor.putString("STATE_FILTER", preferences.state ?: "")
                            editor.putStringSet(
                                "CITY_FILTER",
                                preferences.city?.toSet() ?: emptySet()
                            )
                            editor.putBoolean("PREF_FILTER_APPLIED", true)
                            editor.apply()
                            (requireActivity() as MainActivity).apply {
                                findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId =
                                    R.id.nav_search
                            }

                        }
                }
            }
            binding.tvSeeAllOtherProfiles.setOnClickListener {
                clearFilters()
                (requireActivity() as MainActivity).apply {
//                    setCurrentMenuItem(R.id.nav_search)
                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId =
                        R.id.nav_search
                }
            }

//            requireActivity().runOnUiThread {
            lifecycleScope.launch {
                userProfileViewModel.getNameOfUser(userProfileViewModel.userId)
                    .observe(viewLifecycleOwner) {
                        binding.tvProfileName.text = it
                    }
//                userProfileViewModel.getProfilePic(userProfileViewModel.userId)
//                    .observe(viewLifecycleOwner) {
//                        if (it != null) binding.ivProfilePic.setImageBitmap(it)
//                        else binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)
//                    }


//                }
            }

            binding.cvCity.setOnClickListener {
                clearFilters()
                val editor = sharedPref.edit()
                editor.putBoolean("BASED_ON_CITY_APPLIED", true)
                editor.putString("STATE_FILTER", userProfileViewModel.state)
                editor.putStringSet("CITY_FILTER", setOf(userProfileViewModel.city))
                editor.apply()
                (requireActivity() as MainActivity).apply {
                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId =
                        R.id.nav_search
                }
            }
            binding.cvEducation.setOnClickListener {
                clearFilters()
                val editor = sharedPref.edit()
                editor.putBoolean("BASED_ON_EDUCATION_APPLIED", true)
                editor.putStringSet("EDUCATION_FILTER", setOf(userProfileViewModel.education))
//                editor.putStringSet("CITY_FILTER", setOf(userProfileViewModel.city))
                editor.apply()
                (requireActivity() as MainActivity).apply {
                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId =
                        R.id.nav_search
                }
            }
            binding.cvWork.setOnClickListener {
                clearFilters()
                val editor = sharedPref.edit()
                editor.putBoolean("BASED_ON_OCCUPATION_APPLIED", true)
                editor.putStringSet("OCCUPATION_FILTER", setOf(userProfileViewModel.occupation))
//                editor.putStringSet("CITY_FILTER", setOf(userProfileViewModel.city))
                editor.apply()
                (requireActivity() as MainActivity).apply {
                    findViewById<BottomNavigationView>(R.id.bottom_nav_view).selectedItemId =
                        R.id.nav_search
                }
            }

            binding.homePageLayout.visibility = View.GONE
            binding.loadingScreen.visibility = View.VISIBLE

            setUpNavigationDrawer()
            initSuccessViewPager()
            initPreferredProfiles()
            initOtherProfiles()
            return fragmentView
        }
        return fragmentView
    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {

            userProfileViewModel.getUser(userProfileViewModel.userId).observe(viewLifecycleOwner) {
                userProfileViewModel.city = it.city
                userProfileViewModel.state = it.state
                userProfileViewModel.education = it.education
                userProfileViewModel.occupation = it.occupation
            }
        }

        Log.i(TAG, "homePage onResume")
        val sharedPref =
            requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        if (sharedPref.getBoolean("PREFERENCE_SET", false)) {
            initPreferredProfiles()
            val editor = sharedPref.edit()
            editor.remove("PREFERENCE_SET")
            editor.apply()
        } else if (sharedPref.getBoolean("CLEAR_PARTNER_PREF", false)) {
            initPreferredProfiles()
            Log.i(TAG, "onResume CLEAR_PARTNER_PREF")
            val editor = sharedPref.edit()
            editor.remove("CLEAR_PARTNER_PREF")
            editor.apply()
        }
        if (!userProfileViewModel.profilesLoaded) {
            initPreferredProfiles()
        }
        if(sharedPref.getBoolean("personal_info_updated",false)) {
            setUpNavigationDrawer()
            val editor=sharedPref.edit()
            editor.remove("personal_info_update")
            editor.apply()
        }
    }

    private fun initSuccessViewPager() {
        Log.i(TAG, "initSuccessViewpager")
        val viewPager = binding.viewPagerSuccessStoryImages

        val images = listOf(
            R.drawable.ajith_shalini_wed,
            R.drawable.vijay_sangeetha_wed_15,
            R.drawable.suriya_jo_wed_2,
            R.drawable.nayan_vikki_wed_18,
            R.drawable.kajal_kitchlu_wed_1,
            R.drawable.hanshika_sohael_wed_14,
            R.drawable.aadhi_nikki_wed,
            R.drawable.gautham_manjima_wed_9,
        )

        val coupleNames = listOf(
            "Ajith♥️Shalini",
            "Vijay♥️Sangeetha",
            "Suriya♥️Jyothika",
            "Nayan♥️Vikki",
            "Kajal♥️Kitchlu",
            "Hanshika♥️Sohael",
            "Aadhi♥️Nikki",
            "Gautam♥️Manjima",
        )
        val adapter = AutoImageSwipeAdapter(parentFragmentManager, images, coupleNames)
        viewPager.adapter = adapter
//        viewPager.setPageTransformer(false,SlidePageTransformer())

//        binding.viewPagerSuccessStoryImages.currentItem = userProfileViewModel.currentSwipeImage

        binding.viewPagerSuccessStoryImages.setCurrentItem(
            userProfileViewModel.currentSwipeImage,
            false
        )

        viewPager.setPageTransformer(false, SlidePageTransformer())

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                userProfileViewModel.currentSwipeImage = position
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
//        var isViewPagerTouched :Boolean


        val handler = Handler(Looper.getMainLooper())
        val autoScrollRunnable = object : Runnable {
            override fun run() {
                viewPager.currentItem = (viewPager.currentItem + 1) % viewPager.adapter!!.count
                handler.postDelayed(this, 5000) // Auto-scroll every 5 seconds
            }
        }
        handler.postDelayed(autoScrollRunnable, 5000)

//        var isViewPagerTouched = false
        viewPager.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
//                    isViewPagerTouched = true
                    handler.removeCallbacks(autoScrollRunnable)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                    isViewPagerTouched = false
                    handler.postDelayed(autoScrollRunnable, 5000)
                }
            }
            false
        }


        binding.viewPagerSuccessStoryImages.currentItem = userProfileViewModel.currentSwipeImage

    }


    private fun initUser() {
        requireActivity().runOnUiThread {
            lifecycleScope.launch {
//                binding.tvProfileName.text = userProfileViewModel.getNameOfUser(userProfileViewModel.userId)
            }
        }
    }


    private fun setUpNavigationDrawer() {


        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val headerView = binding.navigationView.getHeaderView(0)

        val profileImage = headerView.findViewById<ImageView>(R.id.iv_profile_pic)
        val profileName = headerView.findViewById<TextView>(R.id.tv_profile_name)
        lifecycleScope.launch {

            userProfileViewModel.getNameOfUser(userProfileViewModel.userId)
                .observe(viewLifecycleOwner) {
                    profileName.text = it
                    binding.tvProfileName.text=it
                }
//            albumViewModel.getProfilePic(userProfileViewModel.userId).observe(viewLifecycleOwner){
//                if (it != null) profileImage.setImageBitmap(it.image)
//                else profileImage.setImageResource(R.drawable.default_profile_pic)
//            }
            userProfileViewModel.getProfilePic(userProfileViewModel.userId)
                .observe(viewLifecycleOwner) {
                    if (it != null) profileImage.setImageBitmap(it)
                    else profileImage.setImageResource(R.drawable.default_profile_pic)
                }
            userProfileViewModel.getUserMobile(userProfileViewModel.userId)
                .observe(viewLifecycleOwner) {
                    headerView.findViewById<TextView>(R.id.tv_matrimony_id).text = it
                }

        }



        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(binding.navigationView)

            when (menuItem.itemId) {
                R.id.menu_my_profile -> {
                    val intent = Intent(activity, ViewProfileActivity::class.java)
                    intent.putExtra("USER_ID", userProfileViewModel.userId)
                    startActivity(intent)

                }
                R.id.menu_shortlisted_profiles -> {
                    (activity as MainActivity).navigateBottomView(R.id.menu_shortlisted_profiles)

                }
                R.id.menu_connections -> {
                    (activity as MainActivity).navigateBottomView(R.id.menu_connections)
                }
                R.id.menu_meetings -> {
                    (activity as MainActivity).navigateBottomView(R.id.menu_meetings)
                }
                R.id.menu_settings -> {
                    val intent = Intent(activity, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_sign_out -> {
                    val signOutDialog = SignOutDialogFragment()
                    signOutDialog.signOutListener =
                        SignOutListener {
                            Intent(requireContext(), SignInActivity::class.java).apply {
                                startActivity(this)
                            }
                            requireActivity().finish()

                            userProfileViewModel.initialLogin = true

                            val sharedPref = requireActivity().getSharedPreferences(
                                MY_SHARED_PREFERENCES, Context.MODE_PRIVATE
                            )
                            val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
                            val editor = sharedPref.edit()

                            editor.remove(CURRENT_USER_ID)
                            editor.remove(CURRENT_USER_GENDER)
                            editor.remove("FILTER_STATUS")

                            editor.apply()


                        }
                    signOutDialog.show(childFragmentManager, tag)
                }
            }
            true
        }

    }


    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun initPreferredProfiles() {

        Log.i(TAG, "initPrefProfiles")
        lifecycleScope.launch {
            partnerPreferenceViewModel.getPartnerPreference(userProfileViewModel.userId)
                .observe(viewLifecycleOwner) { preferences ->

                    Log.i(TAG, "partnerPref ${preferences ?: "null"}")
                    if (preferences == null) {
                        binding.rvPreferredProfiles.visibility = View.GONE
                        binding.tvSeeAllPrefProfiles.visibility = View.GONE
                        binding.noPreferredProfilesMessage.visibility = View.VISIBLE

                        userProfileViewModel.preferredUserIds.clear()

                        initOtherProfiles()
                        return@observe
                    }
                    binding.rvPreferredProfiles.visibility = View.VISIBLE
                    binding.tvSeeAllPrefProfiles.visibility = View.VISIBLE
                    binding.noPreferredProfilesMessage.visibility = View.GONE


                    val profilesRecyclerView = binding.rvPreferredProfiles

                    profilesRecyclerView.layoutManager =
                        GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)


                    val profilesGridAdapter = ProfilesGridAdapter(
                        requireActivity(),
                        userProfileViewModel, settingsViewModel, albumViewModel, viewFullProfile
                    )
                    profilesRecyclerView.adapter = profilesGridAdapter
                    profilesGridAdapter.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    var heightArray =
                        requireContext().resources.getStringArray(R.array.height).toMutableList()


                    val heightFrom = preferences.height_from
                    val heightTo = preferences.height_to
                    heightArray = heightArray.dropWhile { it != heightFrom }.toMutableList()
                    heightArray = heightArray.takeWhile { it != heightTo }.toMutableList()
                    heightArray.add(heightTo)
                    lifecycleScope.launch {
                        val gender = if (userProfileViewModel.gender == "M") "F" else "M"
                        Log.i(TAG, "To fetch gender1 '$gender'")
                        userProfileViewModel.getFilteredUserData(
                            gender,
                            preferences.age_from,
                            preferences.age_to,
                            heightArray.size,
                            heightArray,
                            preferences.marital_status?.size ?: 0,
                            preferences.marital_status ?: emptyList(),
                            preferences.education?.size ?: 0,
                            preferences.education ?: emptyList(),
                            preferences.employed_in?.size ?: 0,
                            preferences.employed_in ?: emptyList(),
                            preferences.occupation?.size ?: 0,
                            preferences.occupation ?: emptyList(),
                            if (preferences.religion == null) 0 else 1,
                            if (preferences.religion != null) mutableListOf(preferences.religion!!) else mutableListOf(),
                            preferences.caste?.size ?: 0,
                            preferences.caste ?: emptyList(),
                            preferences.star?.size ?: 0,
                            preferences.star ?: emptyList(),
                            preferences.zodiac?.size ?: 0,
                            preferences.zodiac ?: emptyList(),
                            if (preferences.state == null) 0 else 1,
                            if (preferences.state != null) mutableListOf(preferences.state!!) else mutableListOf(),
                            preferences.city?.size ?: 0,
                            preferences.city ?: emptyList(),
                            0, 0, 0, 0, 0, 0, ""
                        ).observe(viewLifecycleOwner) {
                            Log.i(TAG, "initPref filteredUser size ${it.size}")
                            userProfileViewModel.preferredUserIds.clear()
                            it.forEach {
                                userProfileViewModel.preferredUserIds.add(it.userId)
                            }
                            Log.i(
                                TAG,
                                "initPref prefProf size ${userProfileViewModel.preferredUserIds.size}"
                            )
                            profilesGridAdapter.setList(it.take(5))//.take(5))
                            if (it.isEmpty()) {
                                binding.rvPreferredProfiles.visibility = View.GONE
                                binding.tvSeeAllPrefProfiles.visibility = View.GONE
                                binding.noPreferredProfilesMessage.visibility = View.VISIBLE
                            }
                            initOtherProfiles()
                        }
                    }
                }
        }

    }

    private fun initOtherProfiles() {


        Log.i(TAG, "initOtherProfiles")
        Log.i(TAG, "prefProfiles ${userProfileViewModel.preferredUserIds.joinToString()}")

        val profilesRecyclerView = binding.rvOtherProfiles

        profilesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)

        val profilesGridAdapter =
            ProfilesGridAdapter(
                requireActivity(),
                userProfileViewModel,
                settingsViewModel,
                albumViewModel,
                viewFullProfile
            )
        profilesRecyclerView.adapter = profilesGridAdapter
        lifecycleScope.launch {
            val gender = if (userProfileViewModel.gender == "M") "F" else "M"
            Log.i(TAG, "To fetch gender2 '$gender'")
            userProfileViewModel.getUsersBasedOnGender(gender).observe(viewLifecycleOwner) {

                val otherProfileList = mutableListOf<UserData>()
                it.forEach {
                    if (!userProfileViewModel.preferredUserIds.contains(it.userId)) {
                        otherProfileList.add(it)
                    }
                }
                if (otherProfileList.isEmpty()) {
                    binding.rvOtherProfiles.visibility = View.GONE
                    binding.tvSeeAllOtherProfiles.visibility = View.GONE
                    binding.noOtherProfilesMessage.visibility = View.VISIBLE
                } else {
                    binding.rvOtherProfiles.visibility = View.VISIBLE
                    binding.tvSeeAllOtherProfiles.visibility = View.VISIBLE
                    binding.noOtherProfilesMessage.visibility = View.GONE
                }
                profilesGridAdapter.setList(otherProfileList.take(5))//.take(5))

                binding.homePageLayout.visibility = View.VISIBLE
                binding.loadingScreen.visibility = View.GONE

                userProfileViewModel.profilesLoaded = true
            }
        }

    }

    private fun clearFilters() {
        val sharedPref =
            requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

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
