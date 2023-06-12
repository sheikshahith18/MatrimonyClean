package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.preffrag

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.matrimony.R
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.EnterPasswordActivity
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsActivity

class AccountSettingsFragment:PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_settings, rootKey)
        (activity as SettingsActivity).supportActionBar?.title = "Account"
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when(preference.key){
            "change_password"->{
                Intent(requireActivity(), EnterPasswordActivity::class.java).apply {
                    putExtra("operation","change_password")
                    startActivity(this)
                }
            }
            "delete_account"->{
                Intent(requireActivity(), EnterPasswordActivity::class.java).apply {
                    putExtra("operation","delete_account")
                    startActivity(this)
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}