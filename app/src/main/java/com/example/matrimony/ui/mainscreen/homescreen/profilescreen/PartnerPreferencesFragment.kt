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
import com.example.matrimony.databinding.FragmentPartnerPreferencesBinding
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen.PartnerPrefEditActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PartnerPreferencesFragment : Fragment() {

    lateinit var binding: FragmentPartnerPreferencesBinding
    private val partnerPrefViewModel by viewModels<PartnerPreferenceViewModel>()
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    private var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (fragmentView == null) {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_partner_preferences,
                container,
                false
            )
            fragmentView = binding.root

            loadPartnerPreferences()

        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userProfileViewModel.userId == userProfileViewModel.currentUserId) {
            binding.imgBtnEdit.visibility = View.VISIBLE
            binding.imgBtnEdit.setOnClickListener {
                val intent = Intent(requireActivity(), PartnerPrefEditActivity::class.java)
                startActivity(intent)
            }
        } else
            binding.imgBtnEdit.visibility = View.GONE
    }


    private fun loadPartnerPreferences() {

        Log.i(TAG, "partner Pref currUser ${userProfileViewModel.currentUserId}")

        lifecycleScope.launch {
            partnerPrefViewModel.getPartnerPreference(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) {
                    Log.i(TAG,"Partner Pref ${if(it==null) "null" else "not null"}")
                    if (it != null) {

                        binding.noPreferenceMessage.visibility = View.GONE
                        binding.partnerPrefView.visibility = View.VISIBLE


                        binding.tvAgeValue.text="${it.age_from}  -  ${it.age_to}"
                        binding.tvHeightValue.text="${it.height_from}  -  ${it.height_to}"
                        binding.tvMaritalStatusValue.text="${it.marital_status?.joinToString()?:"Not Set"}"

                        binding.tvEducationValue.text="${it.education?.joinToString()?:"Not Set"}"
                        binding.tvEmployedInValue.text="${it.employed_in?.joinToString()?:"Not Set"}"
                        binding.tvOccupationValue.text="${it.occupation?.joinToString()?:"Not Set"}"

                        binding.tvReligionValue.text="${it.religion?:"Not Set"}"
                        binding.tvCasteValue.text="${it.caste?.joinToString()?:"Not Set"}"
                        binding.tvStarValue.text="${it.star?.joinToString()?:"Not Set"}"
                        binding.tvZodiacValue.text="${it.zodiac?.joinToString()?:"Not Set"}"


                        binding.tvStateValue.text="${it.state?:"Not Set"}"
                        binding.tvCityValue.text="${it.city?.joinToString()?:"Not Set"}"

                    } else {
                        Log.i(TAG,"Pref Null")

                        binding.noPreferenceMessage.visibility = View.VISIBLE
                        binding.partnerPrefView.visibility = View.GONE
                        if (userProfileViewModel.userId != userProfileViewModel.currentUserId) {
                            binding.tvSetPreferences.visibility = View.GONE
                        } else {
                            binding.tvSetPreferences.visibility = View.VISIBLE
                            binding.tvSetPreferences.setOnClickListener {
                                val intent =
                                    Intent(requireActivity(), PartnerPrefEditActivity::class.java)
                                startActivity(intent)
                            }

                        }
                    }
                }
        }
    }
}