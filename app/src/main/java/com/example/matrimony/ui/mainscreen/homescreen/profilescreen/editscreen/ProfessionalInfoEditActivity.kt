package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityProfessionalInfoEditBinding
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfessionalInfoEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfessionalInfoEditBinding

    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professional_info_edit)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        initEducationSpinner()
//        initEmployedInSpinner()
//        initOccupationSpinner()
//        initAnnualIncomeSpinner()
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        initDropDownList(
            binding.educationSelector,
            resources.getStringArray(R.array.education).toMutableList(),
            "education"
        )
        initDropDownList(
            binding.employedInSelector,
            resources.getStringArray(R.array.employed_in).toMutableList(),
            "employed_in"
        )
        initDropDownList(
            binding.occupationSelector,
            resources.getStringArray(R.array.occupation).toMutableList(),
            "occupation"
        )
        initDropDownList(
            binding.annualIncomeSelector,
            resources.getStringArray(R.array.annual_income).toMutableList(),
            "annual_income"
        )

        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }
        binding.btnDiscardChanges.setOnClickListener {
            finish()
        }
        if(userProfileViewModel.loaded){
            initValues()
            userProfileViewModel.loaded=false
        }
    }

    private fun initValues() {
        lifecycleScope.launch {

            userProfileViewModel.getUser(userProfileViewModel.userId)
                .observe(this@ProfessionalInfoEditActivity) { user ->
                    binding.educationSelector.setText(user.education)
                    binding.employedInSelector.setText(user.employed_in)
                    binding.occupationSelector.setText(user.occupation)
                    binding.annualIncomeSelector.setText(user.annual_income)

                }
        }
    }

    private fun initDropDownList(
        selector: AutoCompleteTextView,
        itemArray: List<Any>,
        key: String?
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
    }

    private fun saveChanges() {

        Log.i(TAG, "user view Model in partner pref edit ${userProfileViewModel.userId}")
        lifecycleScope.launch {
            userProfileViewModel.getUser(userProfileViewModel.userId)
                .observe(this@ProfessionalInfoEditActivity) {
                    lifecycleScope.launch {
                        userProfileViewModel.updateUser(
                            it.user_id,
                            it.name,
                            it.dob,
                            it.religion,
                            it.marital_status,
                            it.no_of_children,
                            it.caste,
                            it.zodiac,
                            it.star,
                            it.state,
                            it.city,
                            it.height,
                            it.physical_status,
                            binding.educationSelector.text.toString(),
                            binding.employedInSelector.text.toString(),
                            binding.occupationSelector.text.toString(),
                            binding.annualIncomeSelector.text.toString(),
                            it.family_status,
                            it.family_type,
                            it.about
                        )

                        finish()
                    }

                }
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