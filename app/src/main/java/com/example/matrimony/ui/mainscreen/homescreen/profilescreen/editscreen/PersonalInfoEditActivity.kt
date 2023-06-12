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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.CustomArrayAdapter
import com.example.matrimony.databinding.ActivityPersonalInfoEditBinding
import com.example.matrimony.db.entities.FamilyDetails
import com.example.matrimony.db.entities.Hobbies
import com.example.matrimony.utils.DatePickerFragment
import com.example.matrimony.utils.DatePickerListener
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.FamilyDetailsViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.HabitsViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.HobbiesViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.example.matrimony.utils.Validator
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PersonalInfoEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityPersonalInfoEditBinding

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val familyDetailsViewModel by viewModels<FamilyDetailsViewModel>()
    private val hobbiesViewModel by viewModels<HobbiesViewModel>()
    private val habitsViewModel by viewModels<HabitsViewModel>()
    private val editScreenViewModel by viewModels<EditScreenViewModel>()

    private lateinit var hobbiesAdapter:CustomArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info_edit)

//        binding.heightSelector.keyListener = null
//        binding.physicalStatusSelector.keyListener = null
//        binding.maritalStatusSelector.keyListener = null
//        binding.childrenCountSelector.keyListener = null
//        binding.hobbiesSelector.keyListener = null
//        binding.drinkingHabitSelector.keyListener = null
//        binding.smokingHabitSelector.keyListener = null
//        binding.foodTypeSelector.keyListener = null
//        binding.brothersCountSelector.keyListener = null
//        binding.marriedBrothersSelector.keyListener = null
//        binding.marriedSistersSelector.keyListener = null
//        binding.sistersCountSelector.keyListener = null
//        binding.familyStatusSelector.keyListener = null
//        binding.familyTypeSelector.keyListener = null
//        binding.stateSelector.keyListener = null
//        binding.citySelector.keyListener = null

        binding.citySelector.keyListener = null

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        initDatePicker()

        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }

        binding.btnDiscardChanges.setOnClickListener {
            finish()
        }

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

//        Toast.makeText(this,"Init Hobby",Toast.LENGTH_SHORT).show()
        initDropDownList(
            binding.heightSelector,
            resources.getStringArray(R.array.height).toMutableList(),
            "height"
        )
        initDropDownList(
            binding.physicalStatusSelector,
            resources.getStringArray(R.array.physical_status).toMutableList()
                .apply { add(0, "Not Set") },
            "physical_status"
        )
        initDropDownList(
            binding.maritalStatusSelector,
            resources.getStringArray(R.array.marital_status).toMutableList()
                .apply { add(0, "Not Set") },
            "marital_status"
        )
        initDropDownList(
            binding.drinkingHabitSelector,
            resources.getStringArray(R.array.habits).toMutableList()
                .apply { add(0, "Not Set") },
            "habit"
        )
        initDropDownList(
            binding.smokingHabitSelector,
            resources.getStringArray(R.array.habits).toMutableList()
                .apply { add(0, "Not Set") },
            "habit"
        )
        initDropDownList(
            binding.foodTypeSelector,
            resources.getStringArray(R.array.food_type).toMutableList()
                .apply { add(0, "Not Set") },
            "food_type"
        )
        initDropDownList(
            binding.hobbiesSelector,
            resources.getStringArray(R.array.hobbies).toMutableList(),
            "hobbies"
        )
        initDropDownList(
            binding.brothersCountSelector,
            resources.getStringArray(R.array.count_0_to_5).toMutableList(),
            "no_of_brothers"
        )
        initDropDownList(
            binding.sistersCountSelector,
            resources.getStringArray(R.array.count_0_to_5).toMutableList(),
            "no_of_sisters"
        )
        initDropDownList(
            binding.familyStatusSelector,
            resources.getStringArray(R.array.family_status).toMutableList()
                .apply { add(0, "Not Set") },
            "family_status"
        )
        initDropDownList(
            binding.familyTypeSelector,
            resources.getStringArray(R.array.family_type).toMutableList()
                .apply { add(0, "Not Set") },
            "family_type"
        )
        initDropDownList(
            binding.stateSelector,
            resources.getStringArray(R.array.state).toMutableList(),
            "state"
        )



//        if (editScreenViewModel.loaded)
            initValues()
//        else
//            hobbiesViewModel.selectedHobbies.forEach {
//                val hobbiesArray=resources.getStringArray(R.array.hobbies)
//                hobbiesAdapter.setSelectedPosition(hobbiesArray.indexOf(it))
//                addChips(binding.hobbiesChipGroup, it, "hobbies")
//            }
    }


    private fun initValues() {
//        editScreenViewModel.loaded=true

        Log.i(TAG+3,"loaded1=${editScreenViewModel.loaded}")

        lifecycleScope.launch {
            userProfileViewModel.getUserMobile(userProfileViewModel.userId)
                .observe(this@PersonalInfoEditActivity) {

//                    if()
                    binding.etContactNum.setText(it)
                }
            userProfileViewModel.getUser(userProfileViewModel.userId)
                .observe(this@PersonalInfoEditActivity) { user ->
                    binding.etAboutDesc.setText(user.about)
                    binding.etEditName.setText(user.name)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = user.dob
                    val dob = dateFormat.format(date)
                    binding.etEditDob.setText(dob)
                    binding.heightSelector.setText(user.height)
                    binding.physicalStatusSelector.setText(user.physical_status)
                    binding.maritalStatusSelector.setText(user.marital_status)
                    if (user.marital_status == "Never Married" || user.marital_status == "Not Set") {
                        binding.tilChildrenCount.visibility = View.GONE
                    } else {
                        binding.tilChildrenCount.visibility = View.VISIBLE
                        binding.childrenCountSelector.setText((user.no_of_children ?: 0).toString())
                        initDropDownList(
                            binding.childrenCountSelector,
                            resources.getStringArray(R.array.count_0_to_5).toMutableList(),
                            "children_count"
                        )
                    }
//                    user.no_of_children?.let {
//                        binding.childrenCountSelector.setText(user.no_of_children.toString())
//                    }
                    binding.familyStatusSelector.setText(user.family_status)
                    binding.familyTypeSelector.setText(user.family_type)
                    binding.stateSelector.setText(user.state)
                    if (user.state == "Others") {
                        binding.tilCity.visibility = View.GONE
                    } else {
                        binding.tilCity.visibility = View.VISIBLE
                        user.city?.let {
                            binding.citySelector.setText(it)
                            binding.citySelector.setText(user.city ?: "")
                            if (binding.citySelector.text.toString().isNotBlank()) {
                                initDropDownList(
                                    binding.citySelector,
                                    getCityArray(user.state) as List<String>,
                                    "city"
                                )
                            }
                        }
                    }
                }

            familyDetailsViewModel.getFamilyDetails(userProfileViewModel.userId)
                .observe(this@PersonalInfoEditActivity) {
                    binding.etFatherName.setText(it?.fathers_name ?: "")
                    binding.etMotherName.setText(it?.mothers_name ?: "")
                    binding.brothersCountSelector.setText((it?.no_of_brothers ?: "").toString())
                    binding.sistersCountSelector.setText((it?.no_of_sisters ?: "").toString())

                    it?.let {

                        if (it.no_of_brothers > 0) {
                            binding.tilMarriedBrothers.visibility = View.VISIBLE
                            val arr = Array(it.no_of_brothers + 1) { it }
//                                IntArray(it.no_of_brothers + 1) { num ->
//                                num
//                            }
                            val count = it.married_brothers ?: 0
                            binding.marriedBrothersSelector.setText(count.toString())
                            initDropDownList(
                                binding.marriedBrothersSelector,
                                arr.toList(),
                                "married_count"
                            )
                        }
                        if (it.no_of_sisters > 0) {
                            binding.tilMarriedSisters.visibility = View.VISIBLE
                            val arr = Array(it.no_of_sisters + 1) { it }
                            val count = it.married_sisters ?: 0
                            binding.marriedSistersSelector.setText(count.toString())
                            initDropDownList(
                                binding.marriedSistersSelector,
                                arr.toList(),
                                "married_count"
                            )
                        }
                    }

//                    if (binding.stateSelector.text.toString().isNotBlank()) {
//                        sharedPref.getStringSet("CITY_FILTER", null)?.forEach {
//                            addChips(binding.cityChipGroup, it, DropdownName.CITY)
//                        }
//                        getCityArray(binding.stateSelector.text.toString())?.let {
//                            initDropDownList(binding.citySelector, it, DropdownName.CITY)
//                            binding.tvCity.visibility = View.VISIBLE
//                            binding.tilCity.visibility = View.VISIBLE
//                        }
//                    }

                }



            hobbiesViewModel.getHobbies(userProfileViewModel.userId)
                .observe(this@PersonalInfoEditActivity) {
                    it.forEach { hobby ->
//                        editScreenViewModel.hobbies.add(hobby)
                        addChips(binding.hobbiesChipGroup, hobby, "hobbies")
                        val hobbiesArray=resources.getStringArray(R.array.hobbies)
//                        hobbiesAdapter.removeSelectedPosition(hobbiesArray.indexOf(value))
                        hobbiesAdapter.setSelectedPosition(hobbiesArray.indexOf(hobby))
                        Log.i(TAG+3,"loaded=${editScreenViewModel.loaded}")
                        if(editScreenViewModel.loaded)
                            hobbiesViewModel.selectedHobbies.clear()
                    }
                    editScreenViewModel.loaded = false
                }

            habitsViewModel.getUserHabits(userProfileViewModel.userId)
                .observe(this@PersonalInfoEditActivity) {
                    binding.drinkingHabitSelector.setText(it?.drinking ?: "")
                    binding.smokingHabitSelector.setText(it?.smoking ?: "")
                    binding.foodTypeSelector.setText(it?.food_type ?: "")
                }


            hobbiesViewModel.selectedHobbies.forEach {
                val hobbiesArray=resources.getStringArray(R.array.hobbies)
                hobbiesAdapter.setSelectedPosition(hobbiesArray.indexOf(it))
                addChips(binding.hobbiesChipGroup, it, "hobbies")
            }

//        hobbiesViewModel.selectedHobbies.clear()
        }


    }

    private fun addChips(chipGroup: ChipGroup, value: String, key: String?) {
        chipGroup.children.forEach {
            val chip = it as Chip
            if (chip.text == value)
                return
        }
        when (key) {
            "hobbies" -> {
//                hobbiesViewModel.selectedHobbies.add(value)
            }
        }
        chipGroup.addView(getChip(value, key))
        val sortedChips = chipGroup.children.toList().sortedBy { (it as Chip).text.toString() }
        chipGroup.removeAllViews()
        sortedChips.forEach { chipGroup.addView(it as Chip) }
    }

    private fun getChip(value: String, key: String?): Chip {
        return Chip(this).apply {
            text = value
            isCloseIconVisible = true
            setOnCloseIconClickListener {
//            setOnClickListener {
                (it.parent as ChipGroup).removeView(it)
                lifecycleScope.launch {
                    hobbiesViewModel.removeHobby(userProfileViewModel.userId, value)
                    editScreenViewModel.hobbies.remove(value)
                }
                when (key) {
                    "hobbies" -> {
//                        Toast.makeText(this@PersonalInfoEditActivity,"$value removed",Toast.LENGTH_SHORT).show()
                        val hobbiesArray=resources.getStringArray(R.array.hobbies)
                        hobbiesAdapter.removeSelectedPosition(hobbiesArray.indexOf(value))
                        hobbiesViewModel.selectedHobbies.remove(value)
                    }
                }
            }
        }
    }


//    private fun initDatePicker() {
//        binding.etEditDob.setOnClickListener {
//            val datePicker = DatePickerFragment(true)
//            datePicker.datePickerListener =
//                DatePickerListener { date -> binding.etEditDob.setText(date) }
//            datePicker.show(supportFragmentManager, "date-picker")
//        }
//    }

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


    private fun initDropDownList(
        selector: AutoCompleteTextView,
        itemArray: List<Any>,
        key: String?
    ) {
//        Toast.makeText(this,"Init Dropdown,$key",Toast.LENGTH_SHORT).show()
//        Log.i(TAG+2,"Init Dropdown,$key")
//        Toast.makeText(this,"Init Dropdown,$key",Toast.LENGTH_SHORT).show()
        selector.keyListener = null
        val arrayAdapter = object : CustomArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            itemArray as List<String>
        )
//        val arrayAdapter = object : ArrayAdapter<Any>(
//            this,
//            android.R.layout.simple_spinner_dropdown_item,
//            itemArray
//        )
        {
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

        if(key=="hobbies")
            hobbiesAdapter=arrayAdapter

        var previousPosition = -1
        selector.setOnItemClickListener { parent, view, position, id ->

            when (key) {
                "marital_status" -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position

                    if (parent.getItemAtPosition(position)
                            .toString() == "Never Married" || parent.getItemAtPosition(position)
                            .toString() == "Not Set"
                    ) {
                        binding.tilChildrenCount.visibility = View.GONE
                        binding.childrenCountSelector.text = null
                    } else {
                        binding.tilChildrenCount.visibility = View.VISIBLE
                        initDropDownList(
                            binding.childrenCountSelector,
                            resources.getStringArray(R.array.count_0_to_5).toMutableList(),
                            "children_count"
                        )
                    }
                }
                "state" -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    binding.citySelector.setText("")

                    val cityArray: List<String> =
                        getCityArray(parent.getItemAtPosition(position).toString())
                            ?: return@setOnItemClickListener
                    binding.citySelector.visibility = View.VISIBLE
                    binding.tilCity.visibility = View.VISIBLE
                    initDropDownList(binding.citySelector, cityArray, "city")
                }
                "no_of_brothers" -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    if (parent.getItemAtPosition(position).toString().toInt() > 0) {
                        binding.tilMarriedBrothers.visibility = View.VISIBLE
                        initDropDownList(
                            binding.marriedBrothersSelector,
                            Array(
                                parent.getItemAtPosition(position).toString().toInt() + 1
                            ) { it }.toList(),
//                            IntArray(
//                                parent.getItemAtPosition(position).toString().toInt()
//                            ) { it }.toList(),
                            "married_count"
                        )
                    } else {
                        binding.tilMarriedBrothers.visibility = View.GONE
                    }
                }
                "no_of_sisters" -> {
                    if (previousPosition == position)
                        return@setOnItemClickListener
                    previousPosition = position
                    if (parent.getItemAtPosition(position).toString().toInt() > 0) {
                        binding.tilMarriedSisters.visibility = View.VISIBLE
                        initDropDownList(
                            binding.marriedSistersSelector,
                            Array(
                                parent.getItemAtPosition(position).toString().toInt() + 1
                            ) { it }.toList(),
                            "married_count"
                        )
                    } else {
                        binding.tilMarriedSisters.visibility = View.GONE
                    }
                }
                "hobbies" -> {
                    Log.i(TAG+2,"Inside Hobbiees")
//                    Toast.makeText(this,"Hobbieees",Toast.LENGTH_SHORT).show()
                    hobbiesAdapter=arrayAdapter
                    arrayAdapter.setSelectedPosition(position)
                    editScreenViewModel.hobbies.add(parent.getItemAtPosition(position).toString())
                    hobbiesViewModel.selectedHobbies.add(parent.getItemAtPosition(position).toString())
                    addChips(
                        binding.hobbiesChipGroup,
                        parent.getItemAtPosition(position).toString(),
                        "hobbies"
                    )
                    hobbiesViewModel.selectedHobbies.add(parent.getItemAtPosition(position).toString())
                    binding.hobbiesSelector.setText("- Select Hobbies -")
                }
                else -> return@setOnItemClickListener
            }
        }
    }



    private fun getCityArray(selectedState: String): List<String>? {
        return when (selectedState) {
            "Andhra Pradesh" -> resources.getStringArray(R.array.andhra_cities).toList()
            "Karnataka" -> resources.getStringArray(R.array.karnataka_cities).toList()
            "Kerala" -> resources.getStringArray(R.array.kerala_cities).toList()
            "Tamilnadu" -> resources.getStringArray(R.array.tn_cities).toList()
            "Others" -> {
                binding.citySelector.text = null
                binding.citySelector.visibility = View.GONE
                binding.tilCity.visibility = View.GONE
                null
            }
            else -> null
        }
    }


    private fun isPageFilled(): Boolean {
        var isFilled = true
        if (binding.etEditName.text.toString().isBlank()) {
            isFilled = false
            binding.tilEditName.isErrorEnabled = true
            binding.tilEditName.error = "Enter full name"
        }
        if (binding.etEditDob.text?.isBlank() == true) {
            isFilled = false
            binding.tilEditDob.isErrorEnabled = true
            binding.tilEditDob.error = "Enter D.O.B"
        }
        if (binding.stateSelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilState.isErrorEnabled = true
            binding.tilState.error = "Select State"
        }
        if (binding.stateSelector.text.toString() != "Others" && binding.citySelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilCity.isErrorEnabled = true
            binding.tilCity.error = "Select City"
        }

        if (!Validator.mobileNumValidator(binding.etContactNum.text.toString())) {
            isFilled = false
            binding.tilContact.isErrorEnabled = true
            binding.tilContact.error = "Enter Valid Mobile No."
        }

        binding.btnSaveChanges.requestFocus()
        return isFilled
    }

    private fun saveChanges() {
        if (isPageFilled()) {
            lifecycleScope.launch {
                userProfileViewModel.getUser(userProfileViewModel.userId)
                    .observe(this@PersonalInfoEditActivity) {

                        lifecycleScope.launch {
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val dob = dateFormat.parse(binding.etEditDob.text.toString())

                            userProfileViewModel.updateUser(
                                userProfileViewModel.userId,
                                binding.etEditName.text.toString().trim(),
                                dob!!,
                                it.religion,
                                binding.maritalStatusSelector.text.toString(),
                                binding.childrenCountSelector.text.toString().ifBlank { "null" }
                                    .toIntOrNull(),
                                it.caste,
                                it.zodiac,
                                it.star,
                                binding.stateSelector.text.toString(),
                                if (binding.citySelector.isVisible) binding.citySelector.text.toString() else null,
                                binding.heightSelector.text.toString(),
                                binding.physicalStatusSelector.text.toString(),
                                it.education,
                                it.employed_in,
                                it.occupation,
                                it.annual_income,
                                binding.familyStatusSelector.text.toString(),
                                binding.familyTypeSelector.text.toString(),
                                binding.etAboutDesc.text.toString().trim(),
                            )
                        }

                        val sharedPref =
                            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)!!
                        val editor = sharedPref.edit()
                        editor.putBoolean("personal_info_updated",true)
                        editor.apply()

                    }

                val drinking =
                    binding.drinkingHabitSelector.text.toString().ifBlank { "Not Set" }
                val smoking = binding.smokingHabitSelector.text.toString().ifBlank { "Not Set" }
                val foodType = binding.foodTypeSelector.text.toString().ifBlank { "Not Set" }

                habitsViewModel.updateHabits(
                    userProfileViewModel.userId,
                    drinking,
                    smoking,
                    foodType
                )

                val fatherName = binding.etFatherName.text.toString().ifBlank { "Not Set" }
                val motherName = binding.etMotherName.text.toString().ifBlank { "Not Set" }
                val noOfBrothers = binding.brothersCountSelector.text.toString().ifBlank { 0 }
                val marriedBrothers =
                    if (binding.tilMarriedBrothers.isVisible) binding.marriedBrothersSelector.text.toString()
                        .ifBlank { 0 } else null
                val noOfSisters = binding.sistersCountSelector.text.toString().ifBlank { 0 }
                val marriedSisters =
                    if (binding.tilMarriedSisters.isVisible) binding.marriedSistersSelector.text.toString()
                        .ifBlank { 0 } else null

                familyDetailsViewModel.setFamilyDetails(
                    FamilyDetails(
                        userProfileViewModel.userId,
                        fatherName,
                        motherName,
                        noOfBrothers.toString().toInt(),
                        marriedBrothers?.toString()?.toInt(),
                        noOfSisters.toString().toInt(),
                        marriedSisters?.toString()?.toInt()
                    )
                )

                Log.i(TAG+3," selectHob  ${hobbiesViewModel.selectedHobbies.joinToString()}")
                hobbiesViewModel.selectedHobbies.forEach { hobby ->
                    hobbiesViewModel.addHobby(Hobbies(0, userProfileViewModel.userId, hobby))
                }

                userProfileViewModel.updateMobile(
                    userProfileViewModel.userId,
                    binding.etContactNum.text.toString()
                )

                finish()
                Toast.makeText(this@PersonalInfoEditActivity,"Personal Info Details Saved",Toast.LENGTH_SHORT).show()
            }
        } else {
//            Toast.makeText(this@PersonalInfoEditActivity, "False", Toast.LENGTH_SHORT).show()
        }
    }


    private fun registerOnFocusListeners() {
        binding.etContactNum.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tilContact.isErrorEnabled = false
            }
        }

        binding.etEditName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tilEditName.isErrorEnabled = false
            }
        }

    }

}