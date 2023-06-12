package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityPrivacySettingsBinding
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacySettingsBinding

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_privacy_settings)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_settings)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        settingsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)
        initValues()

        Log.i(TAG, "Privacy Settings CurrUser ${settingsViewModel.userId}")
        val list = listOf("Everyone", "Connections Only")

        initDropDownList(binding.profilePicSelector, list, "profile_pic")
        initDropDownList(binding.contactNumSelector, list, "contact_num")
        initDropDownList(binding.albumSelector, list, "album")

    }

    var load = true
    private fun initValues() {
        lifecycleScope.launch {
            settingsViewModel.getPrivacySettings(settingsViewModel.userId)
                .observe(this@PrivacySettingsActivity) {
                    if (load) {
                        binding.profilePicSelector.setText(it.view_profile_pic)
                        binding.contactNumSelector.setText(it.view_contact_num)
                        binding.albumSelector.setText(it.view_my_album)
                        load = false
                    }
                }
        }
    }


    private fun initDropDownList(
        selector: AutoCompleteTextView,
        itemArray: List<Any>,
        key: String
    ) {
        selector.keyListener = null
        val arrayAdapter = object : ArrayAdapter<Any>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            itemArray
        ) {
            override fun getFilter(): Filter {
                return object : Filter() {
                    override fun performFiltering(constraint: CharSequence?): FilterResults {
                        val filterResults = FilterResults()
                        filterResults.values = itemArray
                        filterResults.count = itemArray.size
                        return filterResults
                    }

                    override fun publishResults(
                        constraint: CharSequence?,
                        results: FilterResults?
                    ) {
                        notifyDataSetChanged()
                    }
                }
            }
        }
        selector.setAdapter(arrayAdapter)

        var previousPosition = -1
        selector.setOnItemClickListener { parent, view, position, id ->
            if (previousPosition == position)
                return@setOnItemClickListener
            previousPosition = position

            when (key) {
                "profile_pic" -> {

                }
                "contact_num" -> {

                }
                "album" -> {

                }
            }
            settingsViewModel.updatePrivacySettings(
                settingsViewModel.userId,
                binding.profilePicSelector.text.toString(),
                binding.contactNumSelector.text.toString(),
                binding.albumSelector.text.toString(),
            )
            selector.dismissDropDown()

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}