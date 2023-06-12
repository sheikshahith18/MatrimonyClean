package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.CustomArrayAdapter
import com.example.matrimony.databinding.ActivityPartnerPrefEditBinding
import com.example.matrimony.db.entities.PartnerPreferences
import com.example.matrimony.models.DropdownName
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.PartnerPreferenceViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PartnerPrefEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityPartnerPrefEditBinding
    private val partnerPrefViewModel by viewModels<PartnerPreferenceViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    private lateinit var maritalStatusAdapter: CustomArrayAdapter
    private lateinit var educationAdapter: CustomArrayAdapter
    private lateinit var employedInAdapter: CustomArrayAdapter
    private lateinit var occupationAdapter: CustomArrayAdapter
    private lateinit var casteAdapter: CustomArrayAdapter
    private lateinit var starAdapter: CustomArrayAdapter
    private lateinit var zodiacAdapter: CustomArrayAdapter
    private lateinit var cityAdapter: CustomArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_partner_pref_edit)

        binding.btnSetPreferences.setOnClickListener { setPreferences() }
        binding.btnClearPreferences.setOnClickListener {
            if (isValueSet()) {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Clear Preferences")
                    .setMessage("Are you sure want to clear your preferences?")
                    .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->

                        Toast.makeText(this, "Partner Preferences Cleared", Toast.LENGTH_SHORT)
                            .show()
                        clearPreferences()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        binding.ageToSelector.keyListener = null
        binding.heightToSelector.keyListener = null
        binding.casteSelector.keyListener = null
        binding.citySelector.keyListener = null


//        if (!partnerPrefViewModel.loaded)
//        initValues()
//        else {

//        partnerPrefViewModel.selectedCities.forEach {
//            binding.tilCity.visibility = View.VISIBLE
//            binding.tvCityHeader.visibility = View.VISIBLE
//            addChips(binding.cityChipGroup, it, DropdownName.CITY)
//            if (getCityArray(binding.stateSelector.text.toString()) != null)
//                initDropDownList(
//                    binding.citySelector,
//                    getCityArray(binding.stateSelector.text.toString()) as List<String>,
//                    DropdownName.CITY
//                )
//        }
//        partnerPrefViewModel.selectedCastes.forEach {
//            binding.tilCaste.visibility = View.VISIBLE
//            binding.tvCasteHeader.visibility = View.VISIBLE
//            addChips(binding.casteChipGroup, it, DropdownName.CASTE)
//            Toast.makeText(this, "religion=${binding.religionSelector.text}", Toast.LENGTH_SHORT)
//                .show()
//            if (getCasteArray(binding.religionSelector.text.toString()) != null)
//                initDropDownList(
//                    binding.casteSelector,
//                    getCasteArray(binding.religionSelector.text.toString()) as List<String>,
//                    DropdownName.CASTE
//                )
//        }
//
//        partnerPrefViewModel.selectedMaritalStatus.forEach {
//            addChips(binding.maritalStatusChipGroup, it, DropdownName.MARITAL_STATUS)
//        }
//
//        partnerPrefViewModel.selectedEducations.forEach {
//            addChips(binding.educationChipGroup, it, DropdownName.EDUCATION)
//        }
//        partnerPrefViewModel.selectedEmployedIns.forEach {
//            addChips(binding.employedInChipGroup, it, DropdownName.EMPLOYED_IN)
//        }
//        partnerPrefViewModel.selectedOccupation.forEach {
//            addChips(binding.occupationChipGroup, it, DropdownName.OCCUPATION)
//        }
//        if (binding.religionSelector.text.isNotBlank() && binding.religionSelector.text.toString() != "- Select Religion -") {
//            Toast.makeText(this, "Religion notBlank", Toast.LENGTH_SHORT).show()
//            initDropDownList(
//                binding.casteSelector,
//                getCasteArray(binding.religionSelector.text.toString()),
//                DropdownName.CASTE
//            )
//            partnerPrefViewModel.selectedCastes.forEach {
//                addChips(binding.casteChipGroup, it, DropdownName.CASTE)
//            }
//        }
//        partnerPrefViewModel.selectedStars.forEach {
//            addChips(binding.starChipGroup, it, DropdownName.STAR)
//        }
//        partnerPrefViewModel.selectedZodiacs.forEach {
//            addChips(binding.zodiacChipGroup, it, DropdownName.ZODIAC)
//        }
//        if (binding.stateSelector.text.isNotBlank() && binding.citySelector.text.toString() != "- Select City -") {
//            if (binding.stateSelector.text.toString() != "Others") {
//                Log.i(TAG + 3, "state != others")
//                binding.citySelector.visibility = View.VISIBLE
//                binding.tvCityHeader.visibility = View.VISIBLE
//                initDropDownList(
//                    binding.citySelector,
//                    getCityArray(binding.citySelector.text.toString()),
//                    DropdownName.CITY
//                )
//                partnerPrefViewModel.selectedCities.forEach {
//                    addChips(binding.cityChipGroup, it, DropdownName.CITY)
//                }
//            } else {
//                Log.i(TAG + 3, "state blank")
//                binding.citySelector.visibility = View.GONE
//                binding.tvCityHeader.visibility = View.GONE
//                binding.cityChipGroup.removeAllViews()
//            }
//        }

        initDropDownList(
            binding.ageFromSelector,
            Array(52) { it + 18 }.toList(),
            DropdownName.AGE_FROM
        )

//        if(binding.ageFromSelector.text.toString().isNotBlank()){
////            val ageArray = Array(22) { it + 18 }.toMutableList()
//            val selectedFromAge = binding.ageFromSelector.text.toString().toInt()
//            val selectedToAge = binding.ageToSelector.text.toString()
//            if (selectedToAge.isNotBlank()) {
//                if (selectedFromAge >= selectedToAge.toInt())
//                    binding.ageToSelector.setText("")
//            }
//            val ageToArray = Array(45 - selectedFromAge) { it + selectedFromAge + 1 }
//            initDropDownList(
//                binding.ageToSelector,
//                ageToArray.toList(),
//                DropdownName.AGE_TO
//            )
//        }
        initDropDownList(
            binding.heightFromSelector,
            resources.getStringArray(R.array.height).toMutableList().apply { removeAt(size - 1) },
            DropdownName.HEIGHT_FROM
        )
        initDropDownList(
            binding.maritalStatusSelector,
            resources.getStringArray(R.array.marital_status).toList(),
            DropdownName.MARITAL_STATUS
        )
        initDropDownList(
            binding.educationSelector,
            resources.getStringArray(R.array.education).toList(),
            DropdownName.EDUCATION
        )
        initDropDownList(
            binding.employedInSelector,
            resources.getStringArray(R.array.employed_in).toList(),
            DropdownName.EMPLOYED_IN
        )
        initDropDownList(
            binding.occupationSelector,
            resources.getStringArray(R.array.occupation).toList(),
            DropdownName.OCCUPATION
        )
        initDropDownList(
            binding.religionSelector,
            resources.getStringArray(R.array.religion).toList(),
            DropdownName.RELIGION
        )
        initDropDownList(
            binding.starSelector,
            resources.getStringArray(R.array.stars).toList(),
            DropdownName.STAR
        )
        initDropDownList(
            binding.zodiacSelector,
            resources.getStringArray(R.array.zodiac).toList(),
            DropdownName.ZODIAC
        )
        initDropDownList(
            binding.stateSelector,
            resources.getStringArray(R.array.state).toList(),
            DropdownName.STATE
        )

        if (!partnerPrefViewModel.loaded)
            initValues()
        else
            initSelections()

    }

    override fun onResume() {
        super.onResume()
        if (binding.ageFromSelector.text.isBlank())
            binding.tilAgeTo.visibility = View.GONE
        else
            binding.tilAgeTo.visibility = View.VISIBLE

        if (binding.heightFromSelector.text.isBlank())
            binding.tilHeightTo.visibility = View.GONE
        else
            binding.tilHeightTo.visibility = View.VISIBLE
//        if (partnerPrefViewModel.loaded)
        initSelections()
    }


    private fun initValues() {
        partnerPrefViewModel.loaded = true
        Log.i(TAG, "initValues in PartnerPref")

        lifecycleScope.launch {
            partnerPrefViewModel.getPartnerPreference(userProfileViewModel.userId)
                .observe(this@PartnerPrefEditActivity) {
                    if (it != null) {
                        binding.ageFromSelector.setText(it.age_from.toString())
                        binding.ageToSelector.setText(it.age_to.toString().ifBlank { "" })
                        binding.heightFromSelector.setText(it.height_from)
                        binding.heightToSelector.setText(it.height_to)
//                        binding.maritalStatusSelector.setText(it.marital_status ?: "")

                        it.marital_status?.forEach { value ->
                            val maritalStatusArray =
                                resources.getStringArray(R.array.marital_status)
                            maritalStatusAdapter.setSelectedPosition(
                                maritalStatusArray.indexOf(
                                    value
                                )
                            )
                            if (value.isNotBlank())
                                addChips(
                                    binding.maritalStatusChipGroup,
                                    value,
                                    DropdownName.MARITAL_STATUS
                                )
                        }

                        it.education?.forEach { value ->
                            val educationArray = resources.getStringArray(R.array.education)
                            educationAdapter.setSelectedPosition(educationArray.indexOf(value))
                            if (value.isNotBlank())
                                addChips(binding.educationChipGroup, value, DropdownName.EDUCATION)
                        }

                        it.employed_in?.forEach { value ->
                            val employedInArray = resources.getStringArray(R.array.employed_in)
                            employedInAdapter.setSelectedPosition(employedInArray.indexOf(value))
                            if (value.isNotBlank())
                                addChips(
                                    binding.employedInChipGroup,
                                    value,
                                    DropdownName.EMPLOYED_IN
                                )
                        }

                        it.occupation?.forEach { value ->
                            val occupationArray = resources.getStringArray(R.array.occupation)
                            occupationAdapter.setSelectedPosition(occupationArray.indexOf(value))
                            if (value.isNotBlank())
                                addChips(
                                    binding.occupationChipGroup,
                                    value,
                                    DropdownName.OCCUPATION
                                )
                        }

                        binding.religionSelector.setText(it.religion ?: "- Select Religion -")

                        if (it.religion != "Atheism" && it.religion != null && it.religion != "- Select Religion -") {
                            binding.tilCaste.visibility = View.VISIBLE
                            binding.tvCasteHeader.visibility = View.VISIBLE
                            binding.casteSelector.visibility = View.VISIBLE
                            initDropDownList(
                                binding.casteSelector,
                                getCasteArray(it.religion) as List<String>?,
                                DropdownName.CASTE
                            )
                            it.caste?.forEach { value ->
                                val casteArray =
                                    when (it.religion) {
                                        "Hindu" ->
                                            resources.getStringArray(R.array.hindu_caste)
                                        "Muslim" ->
                                            resources.getStringArray(R.array.muslim_caste)
                                        "Christian" ->
                                            resources.getStringArray(R.array.christian_caste)
                                        else -> return@forEach
                                    }
                                casteAdapter.setSelectedPosition(casteArray.indexOf(value))
                                if (value.isNotBlank())
                                    addChips(binding.casteChipGroup, value, DropdownName.CASTE)
                            }
                        } else {
                            binding.tilCaste.visibility = View.GONE
                            binding.tvCasteHeader.visibility = View.GONE
                            binding.casteSelector.visibility = View.GONE
                        }

                        it.star?.forEach { value ->
                            val starArray = resources.getStringArray(R.array.stars)
                            starAdapter.setSelectedPosition(starArray.indexOf(value))
                            if (value.isNotBlank())
                                addChips(binding.starChipGroup, value, DropdownName.STAR)
                        }

                        it.zodiac?.forEach { value ->
                            val zodiacArray = resources.getStringArray(R.array.zodiac)
                            zodiacAdapter.setSelectedPosition(zodiacArray.indexOf(value))
                            if (value.isNotBlank())
                                addChips(binding.zodiacChipGroup, value, DropdownName.ZODIAC)
                        }

                        binding.stateSelector.setText(it.state ?: "- Select City -")

                        if (it.state != "Others" && it.state != null && it.state != "- Select State -") {
                            binding.tvCityHeader.visibility = View.VISIBLE
                            binding.tilCity.visibility = View.VISIBLE
                            binding.citySelector.visibility = View.VISIBLE
                            if (getCityArray(it.state!!) != null)
                                initDropDownList(
                                    binding.citySelector,
                                    getCityArray(it.state!!) as List<String>,
                                    DropdownName.CITY
                                )
                            it.city?.forEach { value ->
                                if (value.isNotBlank())
                                    addChips(binding.cityChipGroup, value, DropdownName.CITY)
                                val cityArray =
                                    when (it.state) {
                                        "Andhra Pradesh" ->
                                            resources.getStringArray(R.array.andhra_cities)
                                        "Kerala" ->
                                            resources.getStringArray(R.array.kerala_cities)
                                        "Karnataka" ->
                                            resources.getStringArray(R.array.karnataka_cities)
                                        "Tamilnadu" ->
                                            resources.getStringArray(R.array.tn_cities)
                                        else -> return@forEach
                                    }
                                cityAdapter.setSelectedPosition(cityArray.indexOf(value))
                            }
//                            Toast.makeText(
//                                this@PartnerPrefEditActivity,
//                                "${it.state}",
//                                Toast.LENGTH_SHORT
//                            ).show()

                        } else {
                            binding.tilCity.visibility = View.GONE
                            binding.tvCityHeader.visibility = View.GONE
                            binding.citySelector.visibility = View.GONE
                        }
                    } else {
                        binding.tvCasteHeader.visibility = View.GONE
                        binding.tilCaste.visibility = View.GONE
                        binding.tilCity.visibility = View.GONE
                    }
////
                    if (binding.ageFromSelector.text.toString().isNotBlank()) {
                        val selectedFromAge = binding.ageFromSelector.text.toString().toInt()
                        val selectedToAge = binding.ageToSelector.text.toString()
                        if (selectedToAge.isNotBlank()) {
                            if (selectedFromAge >= selectedToAge.toInt())
                                binding.ageToSelector.setText("")
                        }
                        val ageToArray = Array(70 - selectedFromAge) { it + selectedFromAge + 1 }
                        Log.i(TAG + 3, "AgeTo ${ageToArray.joinToString()}")
                        initDropDownList(
                            binding.ageToSelector,
                            ageToArray.toList(),
                            DropdownName.AGE_TO
                        )
                    }

                    ////
                    if (binding.heightFromSelector.text.toString().isNotBlank()) {
                        val heightArray = resources.getStringArray(R.array.height).toMutableList()
                        val heightTo = binding.heightToSelector.text.toString()
                        if (heightTo.isNotBlank()) {
                            val fromIndex =
                                heightArray.indexOf(binding.heightFromSelector.text.toString())
                            val toIndex = heightArray.indexOf(heightTo)

                            if (fromIndex >= toIndex)
                                binding.heightToSelector.setText("")
                        }
                        val position =
                            heightArray.indexOf(binding.heightFromSelector.text.toString())
                        heightArray.clear()
                        resources.getStringArray(R.array.height).forEachIndexed { index, value ->
                            if (index > position) {
                                heightArray.add(value)
                            }
                        }
                        initDropDownList(
                            binding.heightToSelector,
                            heightArray,
                            DropdownName.HEIGHT_TO
                        )
                    }


                }
        }

    }

    private fun initSelections() {
        if (binding.ageFromSelector.text.toString().isNotBlank()) {
//            val ageArray = Array(22) { it + 18 }.toMutableList()
            val selectedFromAge = binding.ageFromSelector.text.toString().toInt()
            val selectedToAge = binding.ageToSelector.text.toString()
            if (selectedToAge.isNotBlank()) {
                if (selectedFromAge >= selectedToAge.toInt())
                    binding.ageToSelector.setText("")
            }
            val ageToArray = Array(45 - selectedFromAge) { it + selectedFromAge + 1 }
            Log.i(TAG + 3, "AgeTo ${ageToArray.joinToString()}")
            initDropDownList(
                binding.ageToSelector,
                ageToArray.toList(),
                DropdownName.AGE_TO
            )
        }


        if (binding.heightFromSelector.text.toString().isNotBlank()) {
            val heightArray = resources.getStringArray(R.array.height).toMutableList()
            val heightTo = binding.heightToSelector.text.toString()
            if (heightTo.isNotBlank()) {
                val fromIndex =
                    heightArray.indexOf(binding.heightFromSelector.text.toString())
                val toIndex = heightArray.indexOf(heightTo)

                if (fromIndex >= toIndex)
                    binding.heightToSelector.setText("")
            }
            val position = heightArray.indexOf(binding.heightFromSelector.text.toString())
            heightArray.clear()
            resources.getStringArray(R.array.height).forEachIndexed { index, value ->
                if (index > position) {
                    heightArray.add(value)
                }
            }
            initDropDownList(binding.heightToSelector, heightArray, DropdownName.HEIGHT_TO)
        }

        partnerPrefViewModel.selectedMaritalStatus.forEach {
            val maritalArray = resources.getStringArray(R.array.marital_status)
            maritalStatusAdapter.setSelectedPosition(maritalArray.indexOf(it))
            addChips(binding.maritalStatusChipGroup, it, DropdownName.MARITAL_STATUS)
        }

        partnerPrefViewModel.selectedEducations.forEach {
            val eduArray = resources.getStringArray(R.array.education)
            educationAdapter.setSelectedPosition(eduArray.indexOf(it))
            addChips(binding.educationChipGroup, it, DropdownName.EDUCATION)
        }

        partnerPrefViewModel.selectedEmployedIns.forEach {
            val employedInArray = resources.getStringArray(R.array.employed_in)
            employedInAdapter.setSelectedPosition(employedInArray.indexOf(it))
            addChips(binding.employedInChipGroup, it, DropdownName.EMPLOYED_IN)
        }

        partnerPrefViewModel.selectedOccupation.forEach {
            val occArray = resources.getStringArray(R.array.occupation)
            occupationAdapter.setSelectedPosition(occArray.indexOf(it))
            addChips(binding.occupationChipGroup, it, DropdownName.OCCUPATION)
        }

        if (binding.religionSelector.text.toString().isNotBlank() && binding.religionSelector.text.toString()!="- Select Religion -") {
            binding.tilCaste.visibility = View.VISIBLE
            binding.tvCasteHeader.visibility = View.VISIBLE
            Log.i(TAG + 3, "before caste Init")
            Log.i(TAG + 3, "religion=${binding.religionSelector.text}")
            val casteArray = when (binding.religionSelector.text.toString()) {
                "Muslim" -> getCasteArray("Muslim")
                "Christian" -> getCasteArray("Christian")
                "Hindu" -> getCasteArray("Hindu")
                else -> return
            }
            Log.i(TAG + 3, "caste Init")
            initDropDownList(binding.casteSelector, casteArray, DropdownName.CASTE)
        }
        partnerPrefViewModel.selectedCastes.forEach {
            Log.i(TAG + 3, "selectedCasteSize ${partnerPrefViewModel.selectedCastes.size}")
            val casteArray = when (binding.religionSelector.text.toString()) {
                "Muslim" -> getCasteArray("Muslim")
                "Christian" -> getCasteArray("Christian")
                "Hindu" -> getCasteArray("Hindu")
                else -> return@forEach
            }
            Log.i(TAG + 3, "casteArray ${casteArray?.joinToString()}")
            casteAdapter.setSelectedPosition(casteArray!!.indexOf(it))
            addChips(binding.casteChipGroup, it, DropdownName.CASTE)
        }

        partnerPrefViewModel.selectedStars.forEach {
            val starArray = resources.getStringArray(R.array.stars)
            starAdapter.setSelectedPosition(starArray.indexOf(it))
            addChips(binding.starChipGroup, it, DropdownName.STAR)
        }

        Log.i(TAG+3,"selectedZodiacs ${partnerPrefViewModel.selectedZodiacs.joinToString()}")
        partnerPrefViewModel.selectedZodiacs.forEach {
            val zodiacArray = resources.getStringArray(R.array.zodiac)
            zodiacAdapter.setSelectedPosition(zodiacArray.indexOf(it))
            addChips(binding.zodiacChipGroup, it, DropdownName.ZODIAC)
        }


        if (binding.stateSelector.text.toString().isNotBlank() && binding.stateSelector.text.toString()!="- Select State -") {
            binding.tilCity.visibility = View.VISIBLE
            binding.tvCityHeader.visibility = View.VISIBLE
            val cityArray = when (binding.stateSelector.text.toString()) {
                "Andhra Pradesh" -> getCityArray("Andhra Pradesh")
                "Karnataka" -> getCityArray("Karnataka")
                "Kerala" -> getCityArray("Kerala")
                "Tamilnadu" -> getCityArray("Tamilnadu")
                else -> null
            }
            initDropDownList(binding.citySelector, cityArray, DropdownName.CITY)
        }
        partnerPrefViewModel.selectedCities.forEach {

            val cityArray = when (binding.stateSelector.text.toString()) {
                "Andhra Pradesh" -> getCityArray("Andhra Pradesh")
                "Karnataka" -> getCityArray("Karnataka")
                "Kerala" -> getCityArray("Kerala")
                "Tamilnadu" -> getCityArray("Tamilnadu")
                else -> return@forEach
            }
            cityAdapter.setSelectedPosition(cityArray!!.indexOf(it))
            addChips(binding.cityChipGroup, it, DropdownName.CITY)
        }

    }

    private fun initDropDownList(
        selector: AutoCompleteTextView,
        itemArray: List<Any>?,
        key: DropdownName?
    ) {
        selector.keyListener = null
        if (itemArray == null)
            return
        val arrayAdapter = object : CustomArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            itemArray as List<String>
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

        when (key) {
//            DropdownName.AGE_FROM->
//            DropdownName.AGE_TO->
//            DropdownName.HEIGHT_FROM->
//            DropdownName.HEIGHT_TO->
            DropdownName.MARITAL_STATUS -> maritalStatusAdapter = arrayAdapter
            DropdownName.EDUCATION -> educationAdapter = arrayAdapter
            DropdownName.EMPLOYED_IN -> employedInAdapter = arrayAdapter
            DropdownName.OCCUPATION -> occupationAdapter = arrayAdapter
//            DropdownName.RELIGION->
            DropdownName.CASTE -> casteAdapter = arrayAdapter
            DropdownName.STAR -> starAdapter = arrayAdapter
            DropdownName.ZODIAC -> zodiacAdapter = arrayAdapter
//            DropdownName.STATE->
            DropdownName.CITY -> cityAdapter = arrayAdapter
            else -> {}
        }

        var previousPosition = -1
        selector.setOnItemClickListener { parent, view, position, id ->

            when (key) {
                DropdownName.AGE_FROM -> {
                    binding.tilAgeTo.visibility = View.VISIBLE
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    val ageArray = Array(52) { it + 18 }.toMutableList()
                    val selectedFromAge = binding.ageFromSelector.text.toString().toInt()
                    val selectedToAge = binding.ageToSelector.text.toString()
                    if (selectedToAge.isNotBlank()) {
//                        val fromIndex =
//                            ageArray.indexOf(binding.ageFromSelector.text.toString().toInt())
//                        val toIndex = ageArray.indexOf(selectedToAge.toInt())
////                        if (fromIndex >= toIndex)
////                            binding.ageToSelector.setText("")
                        if (selectedFromAge >= selectedToAge.toInt())
                            binding.ageToSelector.setText("")
                    }
                    val ageToArray = Array(70 - selectedFromAge) { it + selectedFromAge + 1 }
                    initDropDownList(
                        binding.ageToSelector,
                        ageToArray.toList(),
                        DropdownName.AGE_TO
                    )
                }
                DropdownName.HEIGHT_FROM -> {
                    binding.tilHeightTo.visibility = View.VISIBLE
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    val heightArray = resources.getStringArray(R.array.height).toMutableList()
                    val heightTo = binding.heightToSelector.text.toString()
                    if (heightTo.isNotBlank()) {
                        val fromIndex =
                            heightArray.indexOf(binding.heightFromSelector.text.toString())
                        val toIndex = heightArray.indexOf(heightTo)

                        if (fromIndex >= toIndex)
                            binding.heightToSelector.setText("")
                    }

                    heightArray.clear()
                    resources.getStringArray(R.array.height).forEachIndexed { index, value ->
                        if (index > position) {
                            heightArray.add(value)
                        }
                    }
                    initDropDownList(binding.heightToSelector, heightArray, DropdownName.HEIGHT_TO)
                }
                DropdownName.RELIGION -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    binding.casteSelector.setText("")
                    binding.casteChipGroup.removeAllViews()
                    partnerPrefViewModel.selectedCastes.clear()

                    val casteArray: List<String> =
                        getCasteArray(parent.getItemAtPosition(position).toString())
                            ?: return@setOnItemClickListener

                    binding.tvCasteHeader.visibility = View.VISIBLE
                    binding.tilCaste.visibility = View.VISIBLE
                    binding.casteSelector.setText("- Select Caste -")
                    initDropDownList(binding.casteSelector, casteArray, DropdownName.CASTE)
                }
                DropdownName.MARITAL_STATUS -> {
//                    Toast.makeText(this, "Marital Status", Toast.LENGTH_SHORT).show()
                    maritalStatusAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.maritalStatusChipGroup,
                        binding.maritalStatusSelector.text.toString(),
                        DropdownName.MARITAL_STATUS
                    )
                    binding.maritalStatusSelector.setText("- Select Marital Status -")
                }
                DropdownName.STATE -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    binding.citySelector.setText("")
                    binding.cityChipGroup.removeAllViews()
                    partnerPrefViewModel.selectedCities.clear()

                    val cityArray: List<String> =
                        getCityArray(parent.getItemAtPosition(position).toString())
                            ?: return@setOnItemClickListener
                    binding.tvCityHeader.visibility = View.VISIBLE
                    binding.tilCity.visibility = View.VISIBLE
                    binding.citySelector.setText("- Select City -")
                    initDropDownList(binding.citySelector, cityArray, DropdownName.CITY)
                }
                DropdownName.EDUCATION -> {
                    educationAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.educationChipGroup,
                        binding.educationSelector.text.toString(),
                        DropdownName.EDUCATION
                    )
                    binding.educationSelector.setText("- Select Education -")
                }
                DropdownName.EMPLOYED_IN -> {
                    employedInAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.employedInChipGroup,
                        binding.employedInSelector.text.toString(),
                        DropdownName.EMPLOYED_IN
                    )
                    binding.employedInSelector.setText("- Select EmployedIn -")
                }
                DropdownName.OCCUPATION -> {
                    occupationAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.occupationChipGroup,
                        binding.occupationSelector.text.toString(),
                        DropdownName.OCCUPATION
                    )
                    binding.occupationSelector.setText("- Select Occupation -")
                }
                DropdownName.CASTE -> {
                    casteAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.casteChipGroup,
                        binding.casteSelector.text.toString(),
                        DropdownName.CASTE
                    )
                    binding.casteSelector.setText("- Select Caste -")
                }
                DropdownName.STAR -> {
                    starAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.starChipGroup,
                        binding.starSelector.text.toString(),
                        DropdownName.STAR
                    )
                    binding.starSelector.setText("- Select Star -")
                }
                DropdownName.ZODIAC -> {
                    zodiacAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    addChips(
                        binding.zodiacChipGroup,
                        binding.zodiacSelector.text.toString(),
                        DropdownName.ZODIAC
                    )
                    binding.zodiacSelector.setText("- Select Zodiac -")
                }
                DropdownName.CITY -> {
                    cityAdapter = arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
//                    Toast.makeText(this, "City", Toast.LENGTH_SHORT).show()
                    addChips(
                        binding.cityChipGroup,
                        binding.citySelector.text.toString(),
                        DropdownName.CITY
                    )
                    binding.citySelector.setText("- Select City -")
                }
                else -> return@setOnItemClickListener
            }
        }
    }

    private fun getCasteArray(selectedReligion: String?): List<String>? {
        return when (selectedReligion) {
            "Muslim" -> resources.getStringArray(R.array.muslim_caste).toList()
            "Hindu" -> resources.getStringArray(R.array.hindu_caste).toList()
            "Christian" -> resources.getStringArray(R.array.christian_caste)
                .toList()
            "Atheism" -> {
                binding.tvCasteHeader.visibility = View.GONE
                binding.tilCaste.visibility = View.GONE
                null
            }

            else -> null
        }
    }

    private fun getCityArray(selectedState: String): List<String>? {
        return when (selectedState) {
            "Andhra Pradesh" -> resources.getStringArray(R.array.andhra_cities).toList()
            "Karnataka" -> resources.getStringArray(R.array.karnataka_cities).toList()
            "Kerala" -> resources.getStringArray(R.array.kerala_cities).toList()
            "Tamilnadu" -> resources.getStringArray(R.array.tn_cities).toList()
            "Others" -> {
                binding.tvCityHeader.visibility = View.GONE
                binding.tilCity.visibility = View.GONE
                null
            }
            else -> null
        }
    }

    private fun addChips(chipGroup: ChipGroup, value: String, key: DropdownName?) {
        chipGroup.children.forEach {
            val chip = it as Chip
            if (chip.text == value)
                return
        }
        when (key) {
            DropdownName.EDUCATION -> partnerPrefViewModel.selectedEducations.add(value)
            DropdownName.EMPLOYED_IN -> partnerPrefViewModel.selectedEmployedIns.add(value)
            DropdownName.OCCUPATION -> partnerPrefViewModel.selectedOccupation.add(value)
            DropdownName.CASTE -> partnerPrefViewModel.selectedCastes.add(value)
            DropdownName.STAR -> partnerPrefViewModel.selectedStars.add(value)
            DropdownName.ZODIAC -> partnerPrefViewModel.selectedZodiacs.add(value)
            DropdownName.CITY -> partnerPrefViewModel.selectedCities.add(value)
            DropdownName.MARITAL_STATUS -> partnerPrefViewModel.selectedMaritalStatus.add(value)
            else -> return
        }
        chipGroup.addView(getChip(value, key))
        val sortedChips = chipGroup.children.toList().sortedBy { (it as Chip).text.toString() }
        chipGroup.removeAllViews()
        sortedChips.forEach { chipGroup.addView(it as Chip) }
    }

    private fun getChip(value: String, key: DropdownName?): Chip {
        return Chip(this).apply {
            text = value
            isCloseIconVisible = true
            setOnCloseIconClickListener {
//            setOnClickListener {
                (it.parent as ChipGroup).removeView(it)
                when (key) {
                    DropdownName.EDUCATION -> {
                        partnerPrefViewModel.selectedEducations.remove(value)
                        val eduArray = resources.getStringArray(R.array.education)
                        educationAdapter.removeSelectedPosition(eduArray.indexOf(value))
                    }
                    DropdownName.EMPLOYED_IN -> {
                        partnerPrefViewModel.selectedEmployedIns.remove(value)
                        val empArray = resources.getStringArray(R.array.employed_in)
                        employedInAdapter.removeSelectedPosition(empArray.indexOf(value))
                    }
                    DropdownName.OCCUPATION -> {
                        partnerPrefViewModel.selectedOccupation.remove(value)
                        val occArray = resources.getStringArray(R.array.occupation)
                        occupationAdapter.removeSelectedPosition(occArray.indexOf(value))
                    }
                    DropdownName.CASTE -> {
                        partnerPrefViewModel.selectedCastes.remove(value)
                        val casteArray = when (binding.religionSelector.text.toString()) {
                            "Muslim" -> getCasteArray("Muslim")
                            "Christian" -> getCasteArray("Christian")
                            "Hindu" -> getCasteArray("Hindu")
                            else -> return@setOnCloseIconClickListener
                        }
                        casteAdapter.removeSelectedPosition(casteArray!!.indexOf(value))
                    }
                    DropdownName.STAR -> {
                        partnerPrefViewModel.selectedStars.remove(value)
                        val starArray = resources.getStringArray(R.array.stars)
                        starAdapter.removeSelectedPosition(starArray.indexOf(value))
                    }
                    DropdownName.ZODIAC -> {
                        partnerPrefViewModel.selectedZodiacs.remove(value)
                        val zodiacArray = resources.getStringArray(R.array.zodiac)
                        zodiacAdapter.removeSelectedPosition(zodiacArray.indexOf(value))
                    }
                    DropdownName.CITY -> {
                        partnerPrefViewModel.selectedCities.remove(value)
                        val cityArray = when (binding.stateSelector.text.toString()) {
                            "Andhra Pradesh" -> getCityArray("Andhra Pradesh")
                            "Karnataka" -> getCityArray("Karnataka")
                            "Kerala" -> getCityArray("Kerala")
                            "Tamilnadu" -> getCityArray("Tamilnadu")
                            else -> return@setOnCloseIconClickListener
                        }
                        Log.i(TAG + 3, "cityArray ${cityArray?.joinToString()}")
                        cityAdapter.removeSelectedPosition(cityArray!!.indexOf(value))
                    }
                    DropdownName.MARITAL_STATUS -> {
                        partnerPrefViewModel.selectedMaritalStatus.remove(value)
                        val maritalArray = resources.getStringArray(R.array.marital_status)
                        maritalStatusAdapter.removeSelectedPosition(maritalArray.indexOf(value))
                    }
                    else -> return@setOnCloseIconClickListener
                }
            }
        }
    }

    private fun setPreferences() {

//        lifecycleScope.launch {

        if (!isValueSet()) {
            finish()
            return
        }

//        if (isValueSet()) {
        partnerPrefViewModel.addPreference(
            PartnerPreferences(
                userProfileViewModel.userId,
                binding.ageFromSelector.text.toString().ifBlank { 18 }.toString().toInt(),
                binding.ageToSelector.text.toString().ifBlank { 45 }.toString().toInt(),
                binding.heightFromSelector.text.toString().ifBlank { "4 ft 6 in" }.toString(),
                binding.heightToSelector.text.toString().ifBlank { "6 ft" }.toString(),
                partnerPrefViewModel.selectedMaritalStatus.toList().ifEmpty { null },
                partnerPrefViewModel.selectedEducations.toList().ifEmpty { null },
                partnerPrefViewModel.selectedEmployedIns.toList().ifEmpty { null },
                partnerPrefViewModel.selectedOccupation.toList().ifEmpty { null },
                if (binding.religionSelector.text.toString() != "- Select Religion -") binding.religionSelector.text.toString()
                    .ifBlank { null } else null,
                partnerPrefViewModel.selectedCastes.toList().ifEmpty { null },
                partnerPrefViewModel.selectedStars.toList().ifEmpty { null },
                partnerPrefViewModel.selectedZodiacs.toList().ifEmpty { null },
                if (binding.stateSelector.text.toString() != "- Select State -") binding.stateSelector.text.toString()
                    .ifBlank { null } else null,
                partnerPrefViewModel.selectedCities.toList().ifEmpty { null }
            )
        )

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putBoolean("PREFERENCE_SET", true)
        editor.apply()
        finish()

        Toast.makeText(this, "Partner Preferences Updated", Toast.LENGTH_SHORT).show()
//            }
//        }


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

    private fun isValueSet(): Boolean {

        if (binding.ageFromSelector.text.toString().isNotBlank())
            return true
        if (binding.ageToSelector.text.toString().isNotBlank())
            return true
        if (binding.heightFromSelector.text.toString().isNotBlank())
            return true
        if (binding.heightToSelector.text.toString().isNotBlank())
            return true
//        if (binding.maritalStatusSelector.text.toString()
//                .isNotBlank() && binding.maritalStatusSelector.text.toString() != "- Select Marital Status -"
//        )
//            return true
//        if (binding.educationSelector.text.toString()
//                .isNotBlank() && binding.educationSelector.text.toString() != "- Select Education -"
//        )
//            return true
//        if (binding.employedInSelector.text.toString()
//                .isNotBlank() && binding.employedInSelector.text.toString() != "- Select EmployedIn -"
//        )
//            return true
//        if (binding.occupationSelector.text.toString()
//                .isNotBlank() && binding.occupationSelector.text.toString() != "- Select Occupation -"
//        )
//            return true
        if (binding.religionSelector.text.toString()
                .isNotBlank() && binding.religionSelector.text.toString() != "- Select Religion -"
        )
            return true
//        if (binding.casteSelector.text.toString()
//                .isNotBlank() && binding.casteSelector.text.toString() != "- Select Caste -"
//        )
//            return true
//        if (binding.starSelector.text.toString()
//                .isNotBlank() && binding.starSelector.text.toString() != "- Select Star -"
//        )
//            return true
//        if (binding.zodiacSelector.text.toString()
//                .isNotBlank() && binding.zodiacSelector.text.toString() != "- Select Zodiac -"
//        )
//            return true
        if (binding.stateSelector.text.toString()
                .isNotBlank() && binding.stateSelector.text.toString() != "- Select State -"
        )
            return true
//        if (binding.citySelector.text.toString()
//                .isNotBlank() && binding.citySelector.text.toString() != "- Select City -"
//        )
//            return true
        if(binding.maritalStatusChipGroup.childCount>0)
            return true
        if(binding.educationChipGroup.childCount>0)
            return true
        if(binding.employedInChipGroup.childCount>0)
            return true
        if(binding.occupationChipGroup.childCount>0)
            return true
        if(binding.casteChipGroup.childCount>0)
            return true
        if(binding.starChipGroup.childCount>0)
            return true
        if(binding.zodiacChipGroup.childCount>0)
            return true
        if(binding.cityChipGroup.childCount>0)
            return true



        return false
    }


    private fun clearPreferences() {

        binding.tilAgeTo.visibility = View.GONE
        binding.tilHeightTo.visibility = View.GONE

        binding.ageFromSelector.clearFocus()
        binding.ageToSelector.clearFocus()
        binding.heightFromSelector.clearFocus()
        binding.heightToSelector.clearFocus()
        binding.maritalStatusSelector.clearFocus()
        binding.educationSelector.clearFocus()
        binding.employedInSelector.clearFocus()
        binding.occupationSelector.clearFocus()
        binding.religionSelector.clearFocus()
        binding.casteSelector.clearFocus()
        binding.starSelector.clearFocus()
        binding.zodiacSelector.clearFocus()
        binding.stateSelector.clearFocus()
        binding.citySelector.clearFocus()

        maritalStatusAdapter.removeAllSelections()
        educationAdapter.removeAllSelections()
        employedInAdapter.removeAllSelections()
        occupationAdapter.removeAllSelections()
        if (this::casteAdapter.isInitialized)
            casteAdapter.removeAllSelections()
        starAdapter.removeAllSelections()
        zodiacAdapter.removeAllSelections()
        if (this::cityAdapter.isInitialized)
            cityAdapter.removeAllSelections()


        binding.ageToSelector.setText("")
        binding.ageFromSelector.setText("")
        binding.heightToSelector.setText("")
        binding.heightFromSelector.setText("")
//        binding.maritalStatusSelector.setText("")
        binding.maritalStatusChipGroup.removeAllViews()
        binding.educationChipGroup.removeAllViews()
        binding.employedInChipGroup.removeAllViews()
        binding.occupationChipGroup.removeAllViews()
        binding.religionSelector.setText("- Select Religion -")
        binding.casteChipGroup.removeAllViews()
        binding.tvCasteHeader.visibility = View.GONE
        binding.tilCaste.visibility = View.GONE
        binding.starChipGroup.removeAllViews()
        binding.zodiacChipGroup.removeAllViews()
        binding.stateSelector.setText("- Select State -")
        binding.tvCityHeader.visibility = View.GONE
        binding.tilCity.visibility = View.GONE
        binding.cityChipGroup.removeAllViews()

        partnerPrefViewModel.selectedEducations.clear()
        partnerPrefViewModel.selectedEmployedIns.clear()
        partnerPrefViewModel.selectedOccupation.clear()
        partnerPrefViewModel.selectedCastes.clear()
        partnerPrefViewModel.selectedStars.clear()
        partnerPrefViewModel.selectedZodiacs.clear()
        partnerPrefViewModel.selectedCities.clear()
        partnerPrefViewModel.selectedMaritalStatus.clear()

        partnerPrefViewModel.clearPreference(userProfileViewModel.userId)

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("CLEAR_PARTNER_PREF", true)
        editor.apply()

    }

}