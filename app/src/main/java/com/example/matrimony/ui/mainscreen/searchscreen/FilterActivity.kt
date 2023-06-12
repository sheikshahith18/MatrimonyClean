package com.example.matrimony.ui.mainscreen.searchscreen

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.CustomArrayAdapter
import com.example.matrimony.databinding.ActivityFilterBinding
import com.example.matrimony.models.DropdownName
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    lateinit var binding: ActivityFilterBinding
    private val filterViewModel by viewModels<FilterViewModel>()

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
//        Log.i(TAG,"filter onCreate")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)

        binding.btnApplyFilters.setOnClickListener {
            applyFilters()
            filterViewModel.filterChange.value = true
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        initDropDownList(
            binding.ageFromSelector,
            Array(52) { it + 18 }.toList(),
            DropdownName.AGE_FROM
        )

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

        binding.btnClearFilters.setOnClickListener {
            if (isValueSet()) {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Clear Filters")
                    .setMessage("Are you sure want to clear applied filters?")
                    .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->

                                    Toast.makeText(this, "Filters Cleared", Toast.LENGTH_SHORT).show()
//                        cleaPreferences()
                        clearFilters()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
//            clearFilters()
//            filterViewModel.filterChange.value = true
//            Toast.makeText(this, "Filters Cleared", Toast.LENGTH_SHORT).show()
        }

        if (!filterViewModel.loaded)
            initValues()
        else {
            initSelections()
        }

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
//        if (filterViewModel.loaded)
        initSelections()
    }


    private fun initValues() {

        filterViewModel.loaded = true

        binding.ageToSelector.keyListener = null
        binding.heightToSelector.keyListener = null
        binding.casteSelector.keyListener = null
        binding.citySelector.keyListener = null

        Log.i(TAG, "initValues in Filter")
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, MODE_PRIVATE)

        val ageFrom = sharedPref.getInt("AGE_FROM_FILTER", -1)
        if (ageFrom != -1)
            binding.ageFromSelector.setText(ageFrom.toString())

        val ageTo = sharedPref.getInt("AGE_TO_FILTER", -1)
        if (ageTo != -1) {
            binding.ageToSelector.setText(ageTo.toString())
            val ageToArray = Array(40 - ageFrom) { it + ageFrom + 1 }
            initDropDownList(binding.ageToSelector, ageToArray.toMutableList(), DropdownName.AGE_TO)
        }

        binding.heightFromSelector.setText(sharedPref.getString("HEIGHT_FROM_FILTER", ""))
        binding.heightToSelector.setText(sharedPref.getString("HEIGHT_TO_FILTER", ""))
//        binding.maritalStatusSelector.setText(sharedPref.getString("MARITAL_STATUS_FILTER", ""))

        sharedPref.getStringSet("MARITAL_STATUS_FILTER", null)?.forEach {
            addChips(binding.maritalStatusChipGroup, it, DropdownName.MARITAL_STATUS)
        }
        sharedPref.getStringSet("EDUCATION_FILTER", null)?.forEach {
            addChips(binding.educationChipGroup, it, DropdownName.EDUCATION)
        }

        sharedPref.getStringSet("EMPLOYED_IN_FILTER", null)?.forEach {
            addChips(binding.employedInChipGroup, it, DropdownName.EMPLOYED_IN)
        }

        sharedPref.getStringSet("OCCUPATION_FILTER", null)?.forEach {
            addChips(binding.occupationChipGroup, it, DropdownName.OCCUPATION)
        }

        binding.religionSelector.setText(sharedPref.getString("RELIGION_FILTER", "- Select Religion -"))
        if (binding.religionSelector.text.toString().isNotBlank() && binding.religionSelector.text.toString()!="- Select Religion -") {
            sharedPref.getStringSet("CASTE_FILTER", null)?.forEach {
                addChips(binding.casteChipGroup, it, DropdownName.CASTE)
            }
            getCasteArray(binding.religionSelector.text.toString())?.let {
                initDropDownList(binding.casteSelector, it, DropdownName.CASTE)
                binding.tilCaste.visibility = View.VISIBLE
                binding.tvCasteHeader.visibility = View.VISIBLE
            }
        }

        sharedPref.getStringSet("STAR_FILTER", null)?.forEach {
            addChips(binding.starChipGroup, it, DropdownName.STAR)
        }

        sharedPref.getStringSet("ZODIAC_FILTER", null)?.forEach {
            addChips(binding.zodiacChipGroup, it, DropdownName.ZODIAC)
        }

        binding.stateSelector.setText(sharedPref.getString("STATE_FILTER", "- Select State -"))
        if (binding.stateSelector.text.toString().isNotBlank() && binding.stateSelector.text.toString()!="- Select State -") {
            sharedPref.getStringSet("CITY_FILTER", null)?.forEach {
                addChips(binding.cityChipGroup, it, DropdownName.CITY)
            }
            getCityArray(binding.stateSelector.text.toString())?.let {
                initDropDownList(binding.citySelector, it, DropdownName.CITY)
                binding.tvCityHeader.visibility = View.VISIBLE
                binding.tilCity.visibility = View.VISIBLE
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

        filterViewModel.selectedMaritalStatus.forEach {
            val maritalArray = resources.getStringArray(R.array.marital_status)
            maritalStatusAdapter.setSelectedPosition(maritalArray.indexOf(it))
            addChips(binding.maritalStatusChipGroup, it, DropdownName.MARITAL_STATUS)
        }

        filterViewModel.selectedEducations.forEach {
            val eduArray = resources.getStringArray(R.array.education)
            educationAdapter.setSelectedPosition(eduArray.indexOf(it))
            addChips(binding.educationChipGroup, it, DropdownName.EDUCATION)
        }

        filterViewModel.selectedEmployedIns.forEach {
            val employedInArray = resources.getStringArray(R.array.employed_in)
            employedInAdapter.setSelectedPosition(employedInArray.indexOf(it))
            addChips(binding.employedInChipGroup, it, DropdownName.EMPLOYED_IN)
        }

        filterViewModel.selectedOccupation.forEach {
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
        filterViewModel.selectedCastes.forEach {
            Log.i(TAG + 3, "selectedCasteSize ${filterViewModel.selectedCastes.size}")
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

        filterViewModel.selectedStars.forEach {
            val starArray = resources.getStringArray(R.array.stars)
            starAdapter.setSelectedPosition(starArray.indexOf(it))
            addChips(binding.starChipGroup, it, DropdownName.STAR)
        }

        filterViewModel.selectedZodiacs.forEach {
            val zodiacArray = resources.getStringArray(R.array.zodiac)
            zodiacAdapter.setSelectedPosition(zodiacArray.indexOf(it))
            addChips(binding.zodiacChipGroup, it, DropdownName.ZODIAC)
        }


        if (binding.stateSelector.text.toString().isNotBlank() && binding.stateSelector.text.toString()!="- Select State -"){
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
        filterViewModel.selectedCities.forEach {

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
                    filterViewModel.selectedCastes.clear()

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
                    filterViewModel.selectedCities.clear()

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
            DropdownName.EDUCATION -> filterViewModel.selectedEducations.add(value)
            DropdownName.EMPLOYED_IN -> filterViewModel.selectedEmployedIns.add(value)
            DropdownName.OCCUPATION -> filterViewModel.selectedOccupation.add(value)
            DropdownName.CASTE -> filterViewModel.selectedCastes.add(value)
            DropdownName.STAR -> filterViewModel.selectedStars.add(value)
            DropdownName.ZODIAC -> filterViewModel.selectedZodiacs.add(value)
            DropdownName.CITY -> filterViewModel.selectedCities.add(value)
            DropdownName.MARITAL_STATUS -> filterViewModel.selectedMaritalStatus.add(value)
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
                        filterViewModel.selectedEducations.remove(value)
                        val eduArray = resources.getStringArray(R.array.education)
                        educationAdapter.removeSelectedPosition(eduArray.indexOf(value))
                    }
                    DropdownName.EMPLOYED_IN -> {
                        filterViewModel.selectedEmployedIns.remove(value)
                        val empArray = resources.getStringArray(R.array.employed_in)
                        employedInAdapter.removeSelectedPosition(empArray.indexOf(value))
                    }
                    DropdownName.OCCUPATION -> {
                        filterViewModel.selectedOccupation.remove(value)
                        val occArray = resources.getStringArray(R.array.occupation)
                        occupationAdapter.removeSelectedPosition(occArray.indexOf(value))
                    }
                    DropdownName.CASTE -> {
                        filterViewModel.selectedCastes.remove(value)
                        val casteArray = when (binding.religionSelector.text.toString()) {
                            "Muslim" -> getCasteArray("Muslim")
                            "Christian" -> getCasteArray("Christian")
                            "Hindu" -> getCasteArray("Hindu")
                            else -> return@setOnCloseIconClickListener
                        }
                        casteAdapter.removeSelectedPosition(casteArray!!.indexOf(value))
                    }
                    DropdownName.STAR -> {
                        filterViewModel.selectedStars.remove(value)
                        val starArray = resources.getStringArray(R.array.stars)
                        starAdapter.removeSelectedPosition(starArray.indexOf(value))
                    }
                    DropdownName.ZODIAC -> {
                        filterViewModel.selectedZodiacs.remove(value)
                        val zodiacArray = resources.getStringArray(R.array.zodiac)
                        zodiacAdapter.removeSelectedPosition(zodiacArray.indexOf(value))
                    }
                    DropdownName.CITY -> {
                        filterViewModel.selectedCities.remove(value)
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
                        filterViewModel.selectedMaritalStatus.remove(value)
                        val maritalArray = resources.getStringArray(R.array.marital_status)
                        maritalStatusAdapter.removeSelectedPosition(maritalArray.indexOf(value))
                    }
                    else -> return@setOnCloseIconClickListener
                }
            }
        }
    }

    private fun applyFilters() {
        clearPreferences()

        var isFilterApplied = false
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val editor = sharedPref.edit()

        val ageFrom = binding.ageFromSelector.text.toString()
        if (ageFrom.isNotBlank()) {
            val ageTo = binding.ageToSelector.text.toString()
            editor.putInt("AGE_FROM_FILTER", ageFrom.toInt())
            isFilterApplied = true
            if (ageTo.isNotBlank()) {
                editor.putInt("AGE_TO_FILTER", ageTo.toInt())
            }
        }

        val heightFrom = binding.heightFromSelector.text.toString()
        if (heightFrom.isNotBlank()) {
            val heightTo = binding.heightToSelector.text.toString()
            editor.putString("HEIGHT_FROM_FILTER", heightFrom)
            isFilterApplied = true
            if (heightTo.isNotBlank()) {
                editor.putString("HEIGHT_TO_FILTER", heightTo)
            }
        }

//        val maritalStatus = binding.maritalStatusSelector.text.toString()
//        if (maritalStatus.isNotBlank()) {
//            editor.putString("MARITAL_STATUS_FILTER", maritalStatus)
//            isFilterApplied = true
//        }

        if (binding.maritalStatusChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.maritalStatusChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("MARITAL_STATUS_FILTER", set)
        }

        if (binding.educationChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.educationChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("EDUCATION_FILTER", set)
        }

        if (binding.employedInChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.employedInChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("EMPLOYED_IN_FILTER", set)
        }

        if (binding.occupationChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.occupationChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("OCCUPATION_FILTER", set)
        }

        val religion = binding.religionSelector.text.toString()
        if (religion.isNotBlank()  && binding.religionSelector.text.toString()!="- Select Religion -") {
            isFilterApplied = true
            editor.putString("RELIGION_FILTER", religion)
        }

        if (binding.starChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.starChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("STAR_FILTER", set)
        }

        if (binding.zodiacChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.zodiacChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("ZODIAC_FILTER", set)
        }

        val state = binding.stateSelector.text.toString()
        if (state.isNotBlank() && binding.stateSelector.text.toString()!="- Select State -") {
            isFilterApplied = true
            editor.putString("STATE_FILTER", state)
        }

        if (binding.casteChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.casteChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("CASTE_FILTER", set)
        }

        if (binding.cityChipGroup.childCount > 0) {
            val set = mutableSetOf<String>()
            isFilterApplied = true
            binding.cityChipGroup.children.forEach {
                set.add((it as Chip).text.toString())
            }
            editor.putStringSet("CITY_FILTER", set)
        }

        if (isFilterApplied)
            editor.putString("FILTER_STATUS", "applied")

        if (sharedPref.getBoolean("PREF_FILTER_APPLIED", false))
            editor.remove("PREF_FILTER_APPLIED")


        editor.apply()

//        editor.putBoolean("FILTER_STATUS",isFilterApplied)

        Log.i(TAG, "Filter Status:$isFilterApplied")
        val status = sharedPref.getString("FILTER_STATUS", "not_applied")
//        if(status.equals("applied"))
        Log.i(TAG, "Filter Status:$status")


        finish()

    }

    private fun clearPreferences() {
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val previousFilterStatus = sharedPref.getString("FILTER_STATUS", "not_applied")
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
        if (isValueSet() && previousFilterStatus != "not_applied")
            editor.putString("FILTER_STATUS", "cleared")
        else
            editor.putString("FILTER_STATUS", "not_applied")
        editor.apply()
        Log.i(TAG, "clearPref filterStat ${sharedPref.getString("FILTER_STATUS", "null")}")
    }

    private fun clearFilters() {
        clearPreferences()
//        finish()

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

        filterViewModel.selectedEducations.clear()
        filterViewModel.selectedEmployedIns.clear()
        filterViewModel.selectedOccupation.clear()
        filterViewModel.selectedCastes.clear()
        filterViewModel.selectedStars.clear()
        filterViewModel.selectedZodiacs.clear()
        filterViewModel.selectedCities.clear()
        filterViewModel.selectedMaritalStatus.clear()
        
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

        if (binding.religionSelector.text.toString()
                .isNotBlank() && binding.religionSelector.text.toString() != "- Select Religion -"
        )
            return true
        if (binding.stateSelector.text.toString()
                .isNotBlank() && binding.stateSelector.text.toString() != "- Select State -"
        )
            return true
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

}