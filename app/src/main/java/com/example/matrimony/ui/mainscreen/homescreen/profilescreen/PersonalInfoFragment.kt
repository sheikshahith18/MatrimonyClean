package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.FragmentPersonalInfoBinding
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen.PersonalInfoEditActivity
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {

    lateinit var binding: FragmentPersonalInfoBinding
    private var fragmentView: View? = null

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val habitsViewModel by activityViewModels<HabitsViewModel>()
    private val familyDetailsViewModel by activityViewModels<FamilyDetailsViewModel>()
    private val hobbiesViewModel by activityViewModels<HobbiesViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "PersonalInfoFrag onCreate")
        Log.i(TAG, "CurrUser ${userProfileViewModel.userId}")
        if (fragmentView == null) {

            binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_personal_info, container, false)
            fragmentView = binding.root
            loadPersonalInfo()
        }
        return fragmentView
    }

    private fun loadPersonalInfo() {
        lifecycleScope.launch {

            userProfileViewModel.getUserMobile(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) { mobileNo ->
                    if (userProfileViewModel.isUserConnected || userProfileViewModel.userId == userProfileViewModel.currentUserId) {
                        binding.tvContactDesc.text = "+91$mobileNo"
                    } else {
                        lifecycleScope.launch {
                            userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                                .observe(viewLifecycleOwner) {
                                    lifecycleScope.launch {
                                        settingsViewModel.getPrivacySettings(userProfileViewModel.currentUserId)
                                            .observe(viewLifecycleOwner) { privacy ->
                                                if (privacy.view_contact_num == "Everyone") {
                                                    binding.tvContactDesc.text = "+91$mobileNo"
                                                } else if (it == null)
                                                    binding.tvContactDesc.text =
                                                        "+91+91${mobileNo.substring(0, 2)}********"
                                                else if (it == "CONNECTED")
                                                    binding.tvContactDesc.text = "+91$mobileNo"
                                                else
                                                    "+91+91${mobileNo.substring(0, 2)}********"

                                            }
                                    }


                                }

                        }

                    }
                }


            hobbiesViewModel.getHobbies(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) { hobbiesList ->
                    if (hobbiesList.isEmpty()) {
                        binding.tvHobbiesDesc.text = "Not Set"
                    } else {

                        binding.tvHobbiesDesc.text = hobbiesList.toSortedSet().joinToString()
                    }
                }

            habitsViewModel.getUserHabits(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) {

                    if (it == null) {
                        binding.tvDrinkingHabitValue.text = ""
                        binding.tvSmokingHabitValue.text = ""
                        binding.tvFoodTypeValue.text = ""

                        binding.tvDrinkingHabitValue.hint = "Not Set"
                        binding.tvSmokingHabitValue.hint = "Not Set"
                        binding.tvFoodTypeValue.hint = "Not Set"
                    } else {
                        binding.tvDrinkingHabitValue.text =it.drinking.ifBlank { "Not Set" }
                        binding.tvSmokingHabitValue.text =it.smoking.ifBlank { "Not Set" }
                        binding.tvFoodTypeValue.text =it.food_type.ifBlank { "Not Set" }
                    }

                }


            userProfileViewModel.getUser(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) {
                    binding.tvAboutDesc.text = it.about
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = it.dob
                    val dob = dateFormat.format(date)

                    binding.tvNameValue.text=it.name
                    binding.tvAgeValue.text=it.age.toString()
                    binding.tvDobValue.text=dob
                    binding.tvHeightValue.text=it.height
                    binding.tvPhysicalStatusValue.text=it.physical_status
                    binding.tvMaritalStatusValue.text=it.marital_status

                    if(it.marital_status=="Never Married"){
                        binding.tvNoOfChildren.visibility=View.GONE
                        binding.tvNoOfChildrenValue.visibility=View.GONE
                    }else{
                        binding.tvNoOfChildren.visibility=View.VISIBLE
                        binding.tvNoOfChildrenValue.visibility=View.VISIBLE
                        binding.tvNoOfChildrenValue.text=it.no_of_children.toString()
                    }

                    binding.tvStateValue.text=it.state
                    if(it.state=="Others"){
                        binding.tvCity.visibility=View.GONE
                        binding.tvCityValue.visibility=View.GONE
                        binding.tvCityHyphen.visibility=View.GONE
                    }else{
                        binding.tvCity.visibility=View.VISIBLE
                        binding.tvCityValue.visibility=View.VISIBLE
                        binding.tvCityValue.text=it.city
                    }

                }


            familyDetailsViewModel.getFamilyDetails(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) {

                    if(it==null)
                        return@observe
                    binding.tvFatherNameValue.text=it.fathers_name.ifBlank { "Not Set" }
                    binding.tvMotherNameValue.text=it.mothers_name.ifBlank { "Not Set" }
                    binding.tvNoOfBrothersValue.text=it.no_of_brothers.toString()
                    binding.tvNoOfSistersValue.text=it.no_of_sisters.toString()

                    if(it.no_of_brothers==0){
                        binding.tvMarriedBrothers.visibility=View.GONE
                        binding.tvMarriedBrothersValue.visibility=View.GONE
                    }else{
                        binding.tvMarriedBrothers.visibility=View.VISIBLE
                        binding.tvMarriedBrothersValue.visibility=View.VISIBLE
                        binding.tvMarriedBrothersValue.text=it.married_brothers.toString()
                    }

                    if(it.no_of_sisters==0){
                        binding.tvMarriedSisters.visibility=View.GONE
                        binding.tvMarriedSistersValue.visibility=View.GONE
                    }else{
                        binding.tvMarriedSisters.visibility=View.VISIBLE
                        binding.tvMarriedSistersValue.visibility=View.VISIBLE
                        binding.tvMarriedSistersValue.text=it.married_sisters.toString()
                    }

                }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userProfileViewModel.userId == userProfileViewModel.currentUserId) {

            binding.imgBtnEdit.visibility = View.VISIBLE
            binding.imgBtnEdit.setOnClickListener {
                val intent = Intent(requireActivity(), PersonalInfoEditActivity::class.java)
                startActivity(intent)
            }
        } else
            binding.imgBtnEdit.visibility = View.GONE
    }
}