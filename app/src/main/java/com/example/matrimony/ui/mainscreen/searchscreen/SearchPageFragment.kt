package com.example.matrimony.ui.mainscreen.searchscreen

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.SearchPageAdapter
import com.example.matrimony.databinding.FragmentSearchPageBinding
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.models.ConnectionStatus
import com.example.matrimony.models.SortOptions
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionDialogFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionListener
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.utils.CURRENT_USER_GENDER
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.example.matrimony.utils.OnDelayClickListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchPageFragment : Fragment() {

    lateinit var binding: FragmentSearchPageBinding

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val connectionsViewModel by activityViewModels<ConnectionsViewModel>()
    private val settingsViewModel by activityViewModels<SettingsViewModel>()
    private val filterViewModel by activityViewModels<FilterViewModel>()
    private val albumViewModel by activityViewModels<AlbumViewModel>()

    var previousFilterStatus = ""

    private var adapter: SearchPageAdapter? = null
//    private val filterViewModel by activityViewModels<FilterViewModel>()


    private var fragmentView: View? = null

    private lateinit var linearLayoutManager:LinearLayoutManager
    private val recyclerViewPosition=0

    private val profilesRecyclerView by lazy { binding.rvSearchResult }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userProfileViewModel.searchProfilesLoaded = false
        Log.i(TAG, "SearchPage onCreate")
        val sharedPref =
            requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if (fragmentView == null) {

            val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
            userProfileViewModel.userId = userId
            connectionsViewModel.userId = userId
            userProfileViewModel.gender =
                sharedPref.getString(CURRENT_USER_GENDER, null) ?: throw java.lang.Exception("gender invalid")
//        Log.i(TAG, "searchFrag current userId $userId")
//        Log.i(TAG, "searchFrag current user gender ${userProfileViewModel.gender}")

            binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_search_page, container, false)
            fragmentView = binding.root

            binding.etSearch.setOnFocusChangeListener { editText, hasFocus ->
                if (hasFocus) {
                    binding.imgBtnBackArrow.visibility = View.VISIBLE
                    binding.imgBtnSort.visibility = View.GONE
                    binding.imgBtnFilter.visibility = View.GONE
                } else {
                    binding.imgBtnBackArrow.visibility = View.GONE
                    binding.imgBtnSort.visibility = View.VISIBLE
                    binding.imgBtnFilter.visibility = View.VISIBLE
                }
            }

            binding.etSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {

                    val keyboard =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                    keyboard.hideSoftInputFromWindow(v.windowToken, 0)
                    true
                } else false
            }


            binding.etSearch.addTextChangedListener {
                lifecycleScope.launch {
                    delay(300L)
                filterViewModel.sortChange.value = true
                }
            }

            binding.imgBtnBackArrow.setOnClickListener {
                binding.etSearch.apply {
                    clearFocus()
                    setText("")

                    val inputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
                }
            }



            binding.imgBtnFilter.setOnClickListener {
//                showFilterOptions()
                val intent = Intent(requireContext(), FilterActivity::class.java)
                startActivity(intent)
            }

            loadSearchProfiles()

        }

        Log.i(TAG, "${sharedPref.getString("FILTER_STATUS", "not_applied")}")
        return fragmentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBtnSort.setOnClickListener(object : OnDelayClickListener {
            override fun onDelayClick() {
                showSortBottomSheet()
            }
        })




        Log.i(TAG, "SearchPage onViewCreated")
    }

    override fun onPause() {
        super.onPause()

        connectionsViewModel.removeConnection1.forEach{
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeConnection1.clear()
        connectionsViewModel.sendConnectionsTo.forEach {
            connectionsViewModel.addConnection(
                Connections(
                    user_id = connectionsViewModel.userId,
                    connected_user_id = it,
                    status = "REQUESTED"
                )
            )
        }
        connectionsViewModel.sendConnectionsTo.clear()
        connectionsViewModel.removeFromConnections.forEach {
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeFromConnections.clear()
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

    private fun showConfirmConnectionDialog(userId:Int){
        val builder = AlertDialog.Builder(requireActivity())

        builder
//            .setTitle("Connection request pending")
            .setMessage("This user already sent you a request. Do you want to accept it?")
            .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->
                lifecycleScope.launch {
                    connectionsViewModel.setConnectionStatus(userId,"CONNECTED")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private val onConnectionButtonClicked: (Int, String, Int, String) -> Unit =
        { userId: Int, connectionStatus: String, adapterPosition: Int, name: String ->
            when (connectionStatus) {
                "null" -> {
//                    Snackbar.make(binding.root,"Accept the connection request sent by the user", Snackbar.LENGTH_SHORT)
//                        .setAnchorView((requireActivity() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view))
//                        .show()
//                    Toast.makeText(
//                        requireContext(),
//                        "Accept the connection request sent by the user",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    showConfirmConnectionDialog(userId)
                }
                "NOT_CONNECTED" -> {
                    connectedUserId = userId
                    this.adapterPosition = adapterPosition
                    val viewHolder =
                        binding.rvSearchResult.findViewHolderForAdapterPosition(adapterPosition) as SearchPageAdapter.ProfilesViewHolder
//                    viewHolder.btnRemoveConnection.text = "Request Sent"
                    viewHolder.connectionStatus = ConnectionStatus.REQUESTED
                    viewHolder.btnConnection.setImageResource(R.drawable.ic_connection_sent)

                    connectionsViewModel.sendConnectionsTo.add(connectedUserId)
                    connectionsViewModel.removeFromConnections.remove(connectedUserId)
                    Snackbar.make(binding.root,"Connection Request sent to $name", Snackbar.LENGTH_SHORT)
                        .setAnchorView((requireActivity() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view))
                        .show()
                }
                "REQUESTED" -> {
                    connectedUserId = userId
                    this.adapterPosition = adapterPosition
                    val viewHolder =
                        binding.rvSearchResult.findViewHolderForAdapterPosition(adapterPosition) as SearchPageAdapter.ProfilesViewHolder
//                    viewHolder.btnRemoveConnection.text = "Request Sent"
                    viewHolder.connectionStatus = ConnectionStatus.NOT_CONNECTED
                    viewHolder.btnConnection.setImageResource(R.drawable.ic_send_connection)
//                    connectionsViewModel.sendConnectionsTo.remove(userId)

                    CoroutineScope(Dispatchers.Main).launch {
                        if (userProfileViewModel.isConnectionAvailable(connectedUserId)) {
//                            connectionsViewModel.removeConnection(userId)
                            connectionsViewModel.removeConnection1.add(userId)
                        }
                    }
//                    Toast.makeText(requireContext(),"Connection Request sent to $name",Toast.LENGTH_SHORT).show()

                    connectionsViewModel.sendConnectionsTo.remove(connectedUserId)
                    connectionsViewModel.removeFromConnections.add(connectedUserId)

                    Snackbar.make(binding.root,"Connection Request to $name is Cancelled", Snackbar.LENGTH_SHORT)
                        .setAnchorView((requireActivity() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view))
                        .show()
                }
                "CONNECTED" -> {
                    loadDialog(userId, connectionStatus, adapterPosition, name)
                    userProfileViewModel.dialogLoad=true
                    userProfileViewModel.dialogUserId=userId
                    userProfileViewModel.dialogAdapterPosition=adapterPosition
                    userProfileViewModel.dialogUserName=name
                }
            }
        }

    private fun loadDialog(userId: Int, connectionStatus: String, adapterPosition: Int, name: String){
        val dialogFragment = childFragmentManager.findFragmentByTag("remove_connection_dialog") as RemoveConnectionDialogFragment?
        if (dialogFragment != null && dialogFragment.dialog?.isShowing == true) {
            dialogFragment.dialog?.dismiss()
        } else {
            // DialogFragment is not showing or doesn't exist
        }
        connectedUserId = userId
        this.adapterPosition = adapterPosition

        val dialog = RemoveConnectionDialogFragment()
        dialog.removeConnectionListener = RemoveConnectionListener {
            Snackbar.make(binding.root,"Connection with $name is Removed", Snackbar.LENGTH_SHORT)
                .setAnchorView((requireActivity() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view))
                .show()
//                        Toast.makeText(requireContext(), "Remove Click", Toast.LENGTH_SHORT).show()
            val viewHolder =
                binding.rvSearchResult.findViewHolderForAdapterPosition(adapterPosition) as SearchPageAdapter.ProfilesViewHolder
            viewHolder.connectionStatus = ConnectionStatus.NOT_CONNECTED
            viewHolder.btnConnection.setImageResource(R.drawable.ic_send_connection)
            adapter?.notifyDataSetChanged()
//                adapter?.notifyItemChanged(adapterPosition)

            connectionsViewModel.removeConnection(connectedUserId)
//                connectionsViewModel.removeConnection(connectionsViewModel.userId, connectedUserId)
            connectedUserId = -1
        }
        val args = Bundle()
        args.putString("CALLER", this::class.simpleName)
        dialog.arguments = args
        dialog.show(
            childFragmentManager,
            "remove_connection_dialog"
        )
        userProfileViewModel.dialogLoad=true
    }



    private var connectedUserId = -1
    private var adapterPosition = -1


    override fun onResume() {
        super.onResume()
        Log.i(TAG, "SearchPage onResume")

        val dialogFragment = childFragmentManager.findFragmentByTag("remove_connection_dialog") as RemoveConnectionDialogFragment?
        if (dialogFragment == null || dialogFragment.dialog?.isShowing == false) {
            Log.i(TAG+4,"dialogFrag $dialogFragment")
            Log.i(TAG+4,"onRes inside if")
            userProfileViewModel.dialogLoad = false
        }else
            userProfileViewModel.dialogLoad = true
        if(userProfileViewModel.dialogLoad){
            loadDialog(userProfileViewModel.dialogUserId,"",userProfileViewModel.dialogAdapterPosition,userProfileViewModel.dialogUserName)
        }

        val sharedPref =
            requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        if(sharedPref.getBoolean("PREF_FILTER_APPLIED",false)){
            loadSearchProfiles()
            return
        }

        if (sharedPref.getBoolean("FILTER_CLEARED", false)) {
            previousFilterStatus = "not_applied"
            binding.imgBtnFilter.setImageResource(R.drawable.ic_filter_icon)
            val editor = sharedPref.edit()
            editor.remove("FILTER_CLEARED")
            editor.apply()
            loadSearchProfiles()
            return
        }
        val filterStatus = sharedPref.getString("FILTER_STATUS", "not_applied")

//        if(filterStatus.equals("applied")){
//            filterViewModel.filterOptions=FilterOptions.APPLIED
//            applyFilters()
//        }
        Log.i(TAG, "onResume previousStat $previousFilterStatus")
        Log.i(TAG, "onResume filterStat $filterStatus")
        when (filterStatus) {
            "applied" -> {
//                if(!sharedPref.getBoolean("PREF_FILTER_APPLIED",false)) {
                    binding.imgBtnFilter.setImageResource(R.drawable.ic_filter_applied_icon)
//                }else{
//                    val editor=sharedPref.edit()
//                    editor.remove("PREF_FILTER_APPLIED")
//                    editor.apply()
//                }
//                loadSearchProfiles()
                filterViewModel.filterChange.value = true
                previousFilterStatus = filterStatus
            }
            previousFilterStatus -> {

            }
            "cleared" -> {
                binding.imgBtnFilter.setImageResource(R.drawable.ic_filter_icon)
//                loadSearchProfiles()
                filterViewModel.filterChange.value = true
                previousFilterStatus = "not_applied"
                val editor = sharedPref.edit()
                editor.putString("FILTER_STATUS", "not_applied")
                editor.apply()
            }
            "not_applied" -> {
                binding.imgBtnFilter.setImageResource(R.drawable.ic_filter_icon)
//                loadSearchProfiles()
                filterViewModel.filterChange.value = true
                previousFilterStatus = filterStatus
            }
//            "no_change"->{}
        }
//        if (!userProfileViewModel.profilesLoaded) {
//            loadSearchProfiles()
//        }
    }

    private fun showSortBottomSheet() {
        val sortBottomSheetFragment = SortBottomSheetFragment()
        sortBottomSheetFragment.show(parentFragmentManager, "sort_dialog")
    }

    private fun showFilterOptions() {
//        childFragmentManager.beginTransaction().apply {
//            replace(R.id.fragment_container,FilterFragment())
//            addToBackStack(null)
//            commit()
//        }
//        (activity as MainActivity).inflateFragment(FilterFragment())
    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun loadSearchProfiles() {

        binding.searchPageLayout.visibility = View.GONE
        binding.loadingScreen.visibility = View.VISIBLE

//        val profilesRecyclerView = binding.rvSearchResult

            linearLayoutManager=LinearLayoutManager(requireContext())
        profilesRecyclerView.layoutManager =linearLayoutManager
//            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        adapter = SearchPageAdapter(
            requireActivity(),
            userProfileViewModel,
            connectionsViewModel,
            settingsViewModel,
            albumViewModel,
            onConnectionButtonClicked,
            viewFullProfile
        )
        profilesRecyclerView.adapter = adapter
        adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        val gender = if (userProfileViewModel.gender == "M") "F" else "M"
        Log.i(TAG, "SearchFrag genderToDisplay:$gender")
        val sharedPref =
            requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val filterStatus = sharedPref.getString("FILTER_STATUS", "not_applied")

        filterViewModel.filterChange.observe(requireActivity()) {
            Log.i(TAG, "Filter Change Observer- filter status")
            filterViewModel.sortChange.value = true
        }

//        if(filterStatus=="applied"){
////            binding.imgBtnFilter.backgroundTintList= android.R.color.holo_red_dark
//            val badgeDrawable = BadgeDrawable.create(requireContext())
////            badgeDrawable.number = 3
//            badgeDrawable.isVisible = true
//            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.imgBtnFilter)
//        }

        filterViewModel.sortChange.observe(requireActivity()) {
//            Log.i(TAG, "Sort Change Observer")
            Log.i(TAG, "Sort Change Observer- filter status $filterStatus")
//            if (filterStatus.equals("applied")) {

            val ageFrom = sharedPref.getInt("AGE_FROM_FILTER", 18)
            val ageTo = sharedPref.getInt("AGE_TO_FILTER", 45)
            var heightArray =
                requireContext().resources.getStringArray(R.array.height).toMutableList()

            Log.i(TAG, "ageF - $ageFrom-$ageTo")

            val heightFrom = sharedPref.getString("HEIGHT_FROM_FILTER", "4 ft 6 in")
            val heightTo = sharedPref.getString("HEIGHT_TO_FILTER", "6 ft")
            Log.i(TAG, "heightF - $heightFrom - $heightTo")

            heightArray = heightArray.dropWhile { it != heightFrom }.toMutableList()
            heightArray = heightArray.takeWhile { it != heightTo }.toMutableList()
            heightArray.add(heightTo)


            var hValue = ""
            heightArray.forEach {
                hValue += "$it "
            }
            Log.i(TAG, "height - $hValue")

            var value:String?// =
//                sharedPref.getString("MARITAL_STATUS_FILTER", "")
//
//            val maritalStatus =
//                if (value == "" || value == null) listOf<String>() else listOf(value)


            val maritalStatusArray = sharedPref.getStringSet("MARITAL_STATUS_FILTER", setOf())
            val educationArray = sharedPref.getStringSet("EDUCATION_FILTER", setOf())
            val employedIn = sharedPref.getStringSet("EMPLOYED_IN_FILTER", setOf())
            val occupationArray = sharedPref.getStringSet("OCCUPATION_FILTER", setOf())

//            value =
//                sharedPref.getString("ANNUAL_INCOME_FILTER", "")
            var annualIncomeArray = ""
//                if (value == "" || value == null) listOf<String>() else listOf(value)
            sharedPref.getString("ANNUAL_INCOME_FILTER", "")?.let {
                if (it.isNotBlank())
//                    annualIncomeArray.add(it.trim())
                    annualIncomeArray = it
            }

//            value =
//                sharedPref.getString("RELIGION_FILTER", "")
//            val religion =
//                if (value == "" || value == null) listOf<String>() else listOf(value)
            val casteArray = sharedPref.getStringSet("CASTE_FILTER", setOf())
            val starArray = sharedPref.getStringSet("STAR_FILTER", setOf())
            val zodiacArray = sharedPref.getStringSet("ZODIAC_FILTER", setOf())

            value =
                sharedPref.getString("STATE_FILTER", "")
            val stateArray =
                if (value == "" || value == null) listOf<String>() else listOf(value)

            val religionArray = mutableListOf<String>()
            sharedPref.getString("RELIGION_FILTER", "")?.let {
                if (it.isNotBlank())
                    religionArray.add(it)
            }
            val cityArray = sharedPref.getStringSet("CITY_FILTER", setOf())




            Log.i(TAG, "Filters :")
            Log.i(TAG, gender)
            Log.i(TAG, "ageFrom $ageFrom")
            Log.i(TAG, "age To$ageTo")
            Log.i(TAG, "heightSize ${heightArray.size}")
            Log.i(TAG, "height ${heightArray.joinToString()}")
            Log.i(TAG, "maritalSize ${maritalStatusArray?.size}")
            Log.i(TAG, "marital ${maritalStatusArray?.joinToString()}")
            Log.i(TAG, "religionSize ${religionArray.size}")
            Log.i(TAG, "religion ${religionArray.joinToString()}")
            Log.i(TAG, "casteSize ${casteArray!!.size}")
            Log.i(TAG, "caste ${casteArray.joinToString()}")
            Log.i(TAG, "starSize${starArray!!.size}")
            Log.i(TAG, "star ${starArray.joinToString()}")
            Log.i(TAG, "zodiacSize${zodiacArray!!.size}")
            Log.i(TAG, "zodiac ${zodiacArray.joinToString()}")
            Log.i(TAG, "stateSize${stateArray.size}")
            Log.i(TAG, "state${stateArray.joinToString()}")
            Log.i(TAG, "educationSize${educationArray!!.size}")
            Log.i(TAG, "education ${educationArray.joinToString()}")
            Log.i(TAG, "occSize${occupationArray!!.size}")
            Log.i(TAG, "occup ${occupationArray.joinToString()}")
            Log.i(TAG, "annualIncomeSize${annualIncomeArray.isNotBlank()}")
            Log.i(TAG, "annualIncome $annualIncomeArray")
            Log.i(TAG, "employed ${employedIn!!.size}")
            Log.i(TAG, "employed ${employedIn.joinToString()}")
            Log.i(TAG, "city ${cityArray!!.size}")
            Log.i(TAG, "city ${cityArray.joinToString()}")


//        val filter = sharedPref.getString("FILTER_STATUS", "not_applied")
//        if (filter.equals("applied")) {


            lifecycleScope.launch {


//                Log.i(TAG,"Annual Income Arr SIze ${annualIncomeArray.size}")
//                Log.i(TAG,"Annual Income Arr \"${annualIncomeArray[0]}\"")

                Log.i(TAG, "Sort Option ${filterViewModel.sortOptions}")

                userProfileViewModel.getFilteredUserData(
                    gender,
                    ageFrom,
                    ageTo,
                    heightArray.size,
                    heightArray,
                    maritalStatusArray?.size ?: 0,
                    maritalStatusArray?.toList()?: emptyList(),
                    educationArray.size,
                    educationArray.toList(),
                    employedIn.size,
                    employedIn.toList(),
                    occupationArray.size,
                    occupationArray.toList(),
                    religionArray.size,
                    religionArray.toList(),
                    casteArray.size,
                    casteArray.toList(),
                    starArray.size,
                    starArray.toList(),
                    zodiacArray.size,
                    zodiacArray.toList(),
                    stateArray.size,
                    stateArray,
                    cityArray.size,
                    cityArray.toList(),
                    if (filterViewModel.sortOptions == SortOptions.NAME_ASC) 1 else 0,
                    if (filterViewModel.sortOptions == SortOptions.NAME_DESC) 1 else 0,
                    if (filterViewModel.sortOptions == SortOptions.AGE_ASC) 1 else 0,
                    if (filterViewModel.sortOptions == SortOptions.AGE_DESC) 1 else 0,
                    if (filterViewModel.sortOptions == SortOptions.DATE_ASC) 1 else 0,
                    if (filterViewModel.sortOptions == SortOptions.DATE_DESC) 1 else 0,
                    if (binding.etSearch.text.toString().trim().length <= 2) "" else binding.etSearch.text.toString()
                        .trim()

                ).observe(requireActivity()) {
                    Log.i(TAG, "Filtered Profiles Count : ${it.size}")



                    if (it.isEmpty()) {
                        binding.noProfilesMessage.visibility = View.VISIBLE
                        binding.rvSearchResult.visibility = View.GONE
                    } else {
                        adapter?.setList(it)
                        binding.noProfilesMessage.visibility = View.GONE
                        binding.rvSearchResult.visibility = View.VISIBLE
                    }

                    binding.searchPageLayout.visibility = View.VISIBLE
                    binding.loadingScreen.visibility = View.GONE

                    userProfileViewModel.searchProfilesLoaded = true
                }


            }
        }
    }



}