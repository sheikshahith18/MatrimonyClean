package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.preffrag

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.matrimony.R
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsActivity
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrivacySettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel by viewModels<SettingsViewModel>()

    private var profilePicPrivacy:ListPreference?=null
    private var albumPrivacy:ListPreference?=null
    private var contactPrivacy:ListPreference?=null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.privacy_settings, rootKey)
        (activity as SettingsActivity).supportActionBar?.title = "Privacy"

        val sharedPref =
            requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        settingsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValues()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when(preference.key){
            "profile_pic_privacy"->{
//                settingsViewModel.updatePrivacySettings(settingsViewModel.userId,)
            }
            "album_privacy"->{

            }
            "mobile_privacy"->{

            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun initValues() {
        profilePicPrivacy=findPreference("profile_pic_privacy")
        albumPrivacy=findPreference("album_privacy")
        contactPrivacy=findPreference("mobile_privacy")

        lifecycleScope.launch {
            settingsViewModel.getPrivacySettings(settingsViewModel.userId)
                .observe(viewLifecycleOwner) {

                    contactPrivacy?.setValueIndex(if(it.view_contact_num=="Everyone") 0 else 1)
                    contactPrivacy?.summary=if(it.view_contact_num=="Everyone") "Everyone" else "Connections Only"

                    albumPrivacy?.setValueIndex(if(it.view_my_album=="Everyone") 0 else 1)
                    albumPrivacy?.summary=if(it.view_my_album=="Everyone") "Everyone" else "Connections Only"

                    profilePicPrivacy?.setValueIndex(if(it.view_profile_pic=="Everyone") 0 else 1)
                    profilePicPrivacy?.summary=if(it.view_profile_pic=="Everyone") "Everyone" else "Connections Only"
                }

            profilePicPrivacy?.setOnPreferenceChangeListener { preference, newValue ->
                preference.summary = when (newValue) {
                    "0" -> {
                    settingsViewModel.updatePrivacySettings(settingsViewModel.userId,"Everyone",contactPrivacy!!.summary.toString(),albumPrivacy!!.summary.toString())
                        "Everyone"
                    }
                    "1" -> {
                        settingsViewModel.updatePrivacySettings(settingsViewModel.userId,"Connections Only",contactPrivacy!!.summary.toString(),albumPrivacy!!.summary.toString())
                        "Connections Only"
                    }
                    else -> "Everyone"
                }
                true
            }

            albumPrivacy?.setOnPreferenceChangeListener { preference, newValue ->
                preference.summary = when (newValue) {
                    "0" -> {
                        settingsViewModel.updatePrivacySettings(settingsViewModel.userId,profilePicPrivacy!!.summary.toString(),contactPrivacy!!.summary.toString(),"Everyone")
                        "Everyone"
//                    settingsViewModel.updatePrivacySettings(settingsViewModel.userId,)
                    }
                    "1" -> {
                        settingsViewModel.updatePrivacySettings(settingsViewModel.userId,profilePicPrivacy!!.summary.toString(),contactPrivacy!!.summary.toString(),"Connections Only")
                        "Connections Only"
                    }
                    else -> "Everyone"
                }
                true
            }

            contactPrivacy?.setOnPreferenceChangeListener { preference, newValue ->
                preference.summary = when (newValue) {
                    "0" -> {
                        settingsViewModel.updatePrivacySettings(settingsViewModel.userId,profilePicPrivacy!!.summary.toString(),"Everyone",albumPrivacy!!.summary.toString())
                        "Everyone"
//                    settingsViewModel.updatePrivacySettings(settingsViewModel.userId,)
                    }
                    "1" -> {
                        settingsViewModel.updatePrivacySettings(settingsViewModel.userId,profilePicPrivacy!!.summary.toString(),"Connections Only",albumPrivacy!!.summary.toString())
                        "Connections Only"
                    }
                    else -> "Everyone"
                }
                true
            }

        }
    }

}