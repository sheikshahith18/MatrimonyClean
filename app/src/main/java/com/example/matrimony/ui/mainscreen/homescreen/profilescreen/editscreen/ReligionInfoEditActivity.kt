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
import com.example.matrimony.databinding.ActivityReligionInfoEditBinding
import com.example.matrimony.models.DropdownName
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReligionInfoEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityReligionInfoEditBinding
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_religion_info_edit)

//        binding.religionSelector.keyListener = null
//        binding.casteSelector.keyListener = null
//        binding.zodiacSelector.keyListener = null
//        binding.starSelector.keyListener = null

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initValues()

        initDropDownList(
            binding.religionSelector,
            resources.getStringArray(R.array.religion).toMutableList()
                .apply { add(0, "- Not Set -") },
            "religion"
        )
        initDropDownList(
            binding.zodiacSelector,
            resources.getStringArray(R.array.zodiac).toMutableList(),
            "zodiac"
        )
        initDropDownList(
            binding.starSelector,
            resources.getStringArray(R.array.stars).toMutableList(),
            "stars"
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
                .observe(this@ReligionInfoEditActivity) {
                    binding.religionSelector.setText(it.religion)
                    if (it.religion != "- Not Set -" && it.religion != "Atheism") {
                        binding.casteSelector.visibility = View.VISIBLE
                        initDropDownList(
                            binding.casteSelector,
                            getCasteArray(binding.religionSelector.text.toString())!!.toMutableList(),
                            "caste"
                        )
                        binding.casteSelector.setText(it.caste)
                    } else {
                        binding.casteSelector.visibility = View.GONE
                    }
                    binding.starSelector.setText(it.star)
                    binding.zodiacSelector.setText(it.zodiac)
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

        var previousPosition = -1
        selector.setOnItemClickListener { parent, view, position, id ->

            when (key) {
                "religion" -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    binding.casteSelector.setText("")

                    val casteArray: List<String> =
                        getCasteArray(parent.getItemAtPosition(position).toString())
                            ?: return@setOnItemClickListener

                    binding.tilCaste.visibility = View.VISIBLE
                    initDropDownList(binding.casteSelector, casteArray, "caste")
                }
            }
        }
    }

    private fun getCasteArray(selectedReligion: String): List<String>? {
        return when (selectedReligion) {
            "Muslim" -> resources.getStringArray(R.array.muslim_caste).toList()
            "Hindu" -> resources.getStringArray(R.array.hindu_caste).toList()
            "Christian" -> resources.getStringArray(R.array.christian_caste)
                .toList()
            else -> {
//                binding.tvCasteHeader.visibility = View.GONE
                binding.tilCaste.visibility = View.GONE
                null
            }
        }
    }


    private fun saveChanges() {

        lifecycleScope.launch {
            userProfileViewModel.getUser(userProfileViewModel.userId)
                .observe(this@ReligionInfoEditActivity) {
                    lifecycleScope.launch {
                        userProfileViewModel.updateUser(
                            it.user_id,
                            it.name,
                            it.dob,
                            binding.religionSelector.text.toString(),
                            it.marital_status,
                            it.no_of_children,
                            if (binding.casteSelector.visibility == View.VISIBLE) binding.casteSelector.text.toString().ifBlank {
                                binding.tilCaste.isErrorEnabled=true
                                binding.tilCaste.error="Select Caste"
                                return@launch
                            } else null,
                            binding.zodiacSelector.text.toString(),
                            binding.starSelector.text.toString(),
                            it.state,
                            it.city,
                            it.height,
                            it.physical_status,
                            it.education,
                            it.employed_in,
                            it.occupation,
                            it.annual_income,
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