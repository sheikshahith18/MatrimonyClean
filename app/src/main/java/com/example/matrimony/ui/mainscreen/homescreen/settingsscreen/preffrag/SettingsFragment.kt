package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.preffrag

import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import com.example.matrimony.R
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsActivity

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        (activity as SettingsActivity).supportActionBar?.title = "Settings"
        when (rootKey) {
//            addPreferencesFromResource(R.xml.my_settings)
            "settings" -> setPreferencesFromResource(R.xml.settings, rootKey)
            else -> throw java.lang.Exception("Invalid rootKey value")
        }

//        val brightnessSeek = findPreference<SeekBarPreference>("brightness_seek")?.apply {
//            summary = value.toString()
//            setOnPreferenceChangeListener { it, newValue ->
//                it.summary = newValue.toString()
//                true
//            }
//        }
//
//
//        val locationCB = findPreference<CheckBoxPreference>("location_CB")
//        locationCB?.setOnPreferenceChangeListener { it, newValue ->
//            val isChecked = newValue as Boolean
//            if (isChecked) {
//                Toast.makeText(activity, "Location is ON", Toast.LENGTH_SHORT).show()
//                it.summary = "Location ON"
//            } else {
//                Toast.makeText(activity, "Location is OFF", Toast.LENGTH_SHORT).show()
//                it.summary = "Location OFF"
//            }
//            true
//        }
//
//
//        val mediaDownloadOptions =
//            findPreference<MultiSelectListPreference>("media_download_options")
//
//        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
//        val selectedEntries =
//            sharedPref.getStringSet("multi_select_list_preference_key", setOf<String>())
//        mediaDownloadOptions?.summary =
//            if (selectedEntries?.isNotEmpty() == true) selectedEntries.joinToString(", ") else "No media"
//        mediaDownloadOptions?.setOnPreferenceChangeListener { _, newValue ->
//            val newSelectedEntries = newValue as Set<*>
//            sharedPref.edit()
//                .putStringSet("multi_select_list_preference_key", newSelectedEntries as Set<String>)
//                .apply()
//            mediaDownloadOptions.summary =
//                if (newSelectedEntries.isNotEmpty()) newSelectedEntries.joinToString(", ") else "No media"
//            true
//        }
//    }
//
//    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
//        when (preference?.key) {
//            "message_category" -> Toast.makeText(activity, "Message Pref", Toast.LENGTH_SHORT)
//                .show()
//            "sync_category" -> {
//                Toast.makeText(activity, "Sync preference", Toast.LENGTH_SHORT).show()
//                preference.fragment = "com.example.settingsdemo.SyncPrefFragment"
//            }
//        }
//        return super.onPreferenceTreeClick(preference)
    }

    override fun onResume() {
        super.onResume()
        (activity as SettingsActivity).supportActionBar?.title = "Settings"
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when(preference.key){
            "account_settings"->{

            }
            "privacy_settings"->{

            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}