package com.example.matrimony.ui.registerscreen

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.InjectDataViewModel
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivitySignUpBinding
import com.example.matrimony.db.entities.Account
import com.example.matrimony.db.entities.User
import com.example.matrimony.ui.loginscreen.SignInActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.DatePickerFragment
import com.example.matrimony.utils.DatePickerListener
import com.example.matrimony.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel by viewModels<SignUpViewModel>()
    private val injectDataViewModel by viewModels<InjectDataViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        if (signUpViewModel.loadingStarted) {

        }

        binding.btnContinue.setOnClickListener {
            validateAccount()
//            val intent = Intent(this, SignUpNextPageActivity::class.java)
//            startActivity(intent)
        }

//        binding.tvLogin.setOnClickListener {
//            goToLoginPage()
//        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.radioMale.isChecked = true

        binding.etDob.keyListener = null

//        initDatePicker()
        initReligionSpinners()
        initMotherTongueSpinner()
        initStateSpinner()
        registerFocusListeners()

        Log.i(TAG, "onCreate Signup")

        binding.religionSelector.keyListener = null
        binding.casteSelector.keyListener = null
        binding.motherTongueSelector.keyListener = null
        binding.stateSelector.keyListener = null
        binding.citySelector.keyListener = null

        binding.etDob.setOnClickListener {
            initDatePicker()
        }
//        binding.etDob.setOnClickListener {
//            initDatePicker()
//        }
    }

    private fun validateAccount() {
        if (isPageFilled()) {
            var error = false
//                Toast.makeText(this, calculateAge(getDateFromString(binding.etDob.text.toString())!!).toString(),Toast.LENGTH_SHORT).show()
            if (binding.etDob.text.toString()
                    .isNotBlank() && calculateAge(getDateFromString(binding.etDob.text.toString())!!) < 18
            ) {
                binding.tilDob.isErrorEnabled = true
                binding.tilDob.error = "You need to be older than 18 years to register"
                error = true
            } else {
                binding.tilDob.isErrorEnabled = false
            }
            if (!Validator.mobileNumValidator(binding.etMobile.text.toString())) {
                binding.tilMobile.isErrorEnabled = true
                binding.tilMobile.error = "Enter Valid Mobile No."
                error = true
            } else {
                binding.tilMobile.isErrorEnabled = false
            }
            if (!Validator.emailAddressValidator(binding.etEmail.text.toString())) {
                binding.tilEmail.isErrorEnabled = true
                binding.tilEmail.error = "Enter Valid Email Id"
                error = true
            } else {
                binding.tilEmail.isErrorEnabled = false
            }
            if (!Validator.strongPasswordValidator(binding.etPassword.text.toString())) {
                binding.tilPassword.isErrorEnabled = true
                binding.tilPassword.error = "Enter Strong Password"
                error = true
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
            if (!Validator.confirmPasswordValidator(
                    binding.etPassword.text.toString(),
                    binding.etConfirmPass.text.toString()
                )
            ) {
                binding.tilConfirmPass.isErrorEnabled = true
                binding.tilConfirmPass.error = "Passwords didn't match"
                error = true
            } else {
                binding.tilConfirmPass.isErrorEnabled = false
            }
            if (error)
                return
            lifecycleScope.launch {
                error = false
                val isMobileNoUnique =
                    !signUpViewModel.isMobileAlreadyExiists(binding.etMobile.text.toString())
                val isEmailUnique =
                    !signUpViewModel.isEmailAlreadyExists(binding.etEmail.text.toString())
                if (!isEmailUnique) {
                    binding.tilEmail.isErrorEnabled = true
                    binding.tilEmail.error = "Email already exists"
                    error = true
                }
                if (!isMobileNoUnique) {
                    binding.tilMobile.isErrorEnabled = true
                    binding.tilMobile.error = "Mobile No. already exists"
                    error = true
                }
                if (error)
                    return@launch

                startNextActivity()
            }
        }
    }

    private suspend fun startNextActivity() {

        lifecycleScope.launch {

            val sharedPref =
                getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return@launch
//            val loaded=
            val user = User(
//                account.user_id,
                name = binding.etName.text.toString(),
                gender = if (binding.radioMale.isChecked) "M" else "F",
                dob = getDateFromString(binding.etDob.text.toString())!!,
                religion = binding.religionSelector.text.toString(),
                mother_tongue = binding.motherTongueSelector.text.toString(),
                state = binding.stateSelector.text.toString(),
                city = binding.citySelector.text.toString().ifBlank { null },
                profile_pic = null

            )
            val account = Account(
                user_id = user.user_id,
                email = binding.etEmail.text.toString(),
                mobile_no = binding.etMobile.text.toString(),
                password = binding.etPassword.text.toString()
            )
            Log.i(TAG, user.toString())


            var loaded = sharedPref.getInt("DATA_LOADED", -1)
//            Toast.makeText(this@SignUpActivity, "loaded=$loaded", Toast.LENGTH_SHORT).show()
            userProfileViewModel.loaded = false
            if (loaded == -1) {
                binding.loadingScreen.visibility = View.VISIBLE
                binding.signUpScreen.visibility = View.GONE
                injectDataViewModel.context = application
//                injectDataViewModel.addUsers()

                while (injectDataViewModel.count > userProfileViewModel.getNoOfUsers()) {
                    Log.i(
                        TAG,
                        "inject count=${injectDataViewModel.count}, noOfUsers=${userProfileViewModel.getNoOfUsers()}"
                    )
                }
                Log.i(TAG, "MainAct Loaded")
                val editor = sharedPref.edit()
                editor.putInt("DATA_LOADED", 1)
                userProfileViewModel.loaded = true
                loaded = 1
                editor.apply()
            } else {
                binding.loadingScreen.visibility = View.GONE
                binding.signUpScreen.visibility = View.VISIBLE
            }

            signUpViewModel.createAccount(account, user)

            val userId = signUpViewModel.getUserByEmail(binding.etEmail.text.toString())
            val editor = sharedPref.edit()
            editor.putInt(
                CURRENT_USER_ID,
                userId
            )
            editor.putString(
                CURRENT_USER_GENDER,
                user.gender
            )
            editor.apply()

//        GlobalScope.launch {
//            delay(5000)
//            val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
//            val editor = sharedPref.edit()
//            editor.putInt(
//                CURRENT_USER_ID,
//                signUpViewModel.getUserByEmail(binding.etEmail.text.toString())
//            )
            editor.putString(CURRENT_USER_GENDER, user.gender)
//            editor.apply()
//        }

            val intent = Intent(this@SignUpActivity, SignUpNextPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, SignInActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

    private fun initDatePicker() {
//        binding.imgPickDate.setOnClickListener {
        val datePicker = DatePickerFragment(null, binding.etDob.text.toString().ifBlank { null })
        datePicker.datePickerListener =
            DatePickerListener { date ->
                binding.etDob.setText(date)
//                Toast.makeText(
//                    this@SignUpActivity,
//                    calculateAge(getDateFromString(binding.etDob.text.toString())!!).toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        datePicker.show(supportFragmentManager, "date-picker")
//        }
    }

    private fun goToLoginPage() {
        onBackPressed()
    }


    private fun registerFocusListeners() {
        //name
        binding.etName.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.tilName.isErrorEnabled = false
            }
        }

        //dob
        binding.etDob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etDob.text?.isNotBlank() == true)
                    binding.tilDob.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //religion
        binding.religionSelector.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.tilReligion.isErrorEnabled = false
        }

        //caste
        binding.casteSelector.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.tilCaste.isErrorEnabled = false
        }

        //mother tongue
        binding.motherTongueSelector.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.tilMotherTongue.isErrorEnabled = false
        }

        //state
        binding.stateSelector.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.tilState.isErrorEnabled = false
        }

        //city
        binding.citySelector.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.tilCity.isErrorEnabled = false
        }

        //mobile
        binding.etMobile.setOnFocusChangeListener { v, hasFocus ->
//            if (binding.etMobile.text?.isBlank() == true)
//                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.mobileNumValidator(binding.etMobile.text.toString())) {
                    binding.tilMobile.isErrorEnabled = true
                    binding.tilMobile.error = "Enter Valid Mobile No."
                } else {
                    binding.tilMobile.isErrorEnabled = false
                }
            } else {
                binding.tilMobile.isErrorEnabled = false
            }
        }

        //email
        binding.etEmail.setOnFocusChangeListener { v, hasFocus ->
//            if (binding.etEmail.text?.isBlank() == true)
//                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.emailAddressValidator(binding.etEmail.text.toString())) {
                    binding.tilEmail.isErrorEnabled = true
                    binding.tilEmail.error = "Enter Valid Email"
                } else {
                    binding.tilEmail.isErrorEnabled = false
                }
            } else {
                binding.tilEmail.isErrorEnabled = false
            }
        }


        //password
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (binding.etPassword.text?.isBlank() == true)
                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.strongPasswordValidator(binding.etPassword.text.toString())) {
                    binding.tilPassword.isErrorEnabled = true
                    binding.tilPassword.error = "Enter Strong Password"
                } else {
                    binding.tilPassword.isErrorEnabled = false
                }
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
        }

        //confirm password
        binding.etConfirmPass.setOnFocusChangeListener { v, hasFocus ->
            if (binding.etConfirmPass.text?.isBlank() == true)
                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.confirmPasswordValidator(
                        binding.etPassword.text.toString(),
                        binding.etConfirmPass.text.toString()
                    )
                ) {
                    binding.tilConfirmPass.isErrorEnabled = true
                    binding.tilConfirmPass.error = "Passwords didn't match"
                } else {
                    binding.tilConfirmPass.isErrorEnabled = false
                }
            } else {
                binding.tilConfirmPass.isErrorEnabled = false
            }
        }

    }

    private fun initReligionSpinners() {
        val religionArray = resources.getStringArray(R.array.religion)
        val religionArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                religionArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = religionArray
                            filterResults.count = religionArray.size
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
        binding.religionSelector.setAdapter(religionArrayAdapter)

        var selectedReligion: String
        var previousPosition = -1
        binding.religionSelector.setOnItemClickListener { parent, view, position, id ->
            if (previousPosition == position)
                return@setOnItemClickListener
            Log.i(TAG, "religion:${binding.religionSelector.text.toString()}")
            previousPosition = position
            signUpViewModel.religionPosition = position
            selectedReligion = parent.getItemAtPosition(position).toString()
            binding.casteSelector.setText("")
            initCasteSpinner(position)
            Log.i(TAG, selectedReligion)
        }

        if (signUpViewModel.religionPosition != -1)
            initCasteSpinner(signUpViewModel.religionPosition)
    }

    private fun initCasteSpinner(selectedPosition: Int) {
        var casteArray = arrayOf("")
        when (selectedPosition) {
            0 -> casteArray = resources.getStringArray(R.array.muslim_caste)
            1 -> casteArray = resources.getStringArray(R.array.hindu_caste)
            2 -> casteArray = resources.getStringArray(R.array.christian_caste)
            3 -> {
                binding.casteSelector.visibility = View.GONE
                binding.tilCaste.visibility = View.GONE
                return
            }
        }
        val casteArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                casteArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = casteArray
                            filterResults.count = casteArray.size
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

        binding.casteSelector.visibility = View.VISIBLE
        binding.tilCaste.visibility = View.VISIBLE
        binding.casteSelector.setAdapter(casteArrayAdapter)
    }

    private fun initMotherTongueSpinner() {
        val languageArray = resources.getStringArray(R.array.languages)
        val languagesArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                languageArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = languageArray
                            filterResults.count = languageArray.size
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
        binding.motherTongueSelector.setAdapter(languagesArrayAdapter)
    }

    private fun initStateSpinner() {
        val statesArray = resources.getStringArray(R.array.state)
        val statesArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statesArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = statesArray
                            filterResults.count = statesArray.size
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
        binding.stateSelector.setAdapter(statesArrayAdapter)

        var selectedState: String
        var previousPosition = -1
        binding.stateSelector.setOnItemClickListener { parent, view, position, id ->
            if (previousPosition == position)
                return@setOnItemClickListener
            previousPosition = position
            selectedState = parent.getItemAtPosition(position).toString()
            binding.citySelector.setText("")
            signUpViewModel.statePosition = position
            initCitySpinner(position)
            Log.i(TAG, selectedState)
        }
        if (signUpViewModel.statePosition != -1)
            initCitySpinner(signUpViewModel.statePosition)
    }

    private fun initCitySpinner(selectedStatePosition: Int) {
        var cityArray = arrayOf("")
        when (selectedStatePosition) {
            0 -> cityArray = resources.getStringArray(R.array.andhra_cities)

            1 -> cityArray = resources.getStringArray(R.array.karnataka_cities)
            2 -> cityArray = resources.getStringArray(R.array.kerala_cities)
            3 -> cityArray = resources.getStringArray(R.array.tn_cities)
            4 -> {
                binding.citySelector.visibility = View.GONE
                binding.tilCity.visibility = View.GONE
                return
            }
        }
        val cityArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                cityArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = cityArray
                            filterResults.count = cityArray.size
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

        binding.citySelector.visibility = View.VISIBLE
        binding.tilCity.visibility = View.VISIBLE
        binding.citySelector.setAdapter(cityArrayAdapter)
    }

    private fun isPageFilled(): Boolean {
        var isFilled = true
        if (binding.etName.text?.isBlank() == true) {
            isFilled = false
            binding.tilName.isErrorEnabled = true
            binding.tilName.error = "Enter full name"
        }
        if (binding.etDob.text?.isBlank() == true) {
            isFilled = false
            binding.tilDob.isErrorEnabled = true
            binding.tilDob.error = "Enter D.O.B"
        }
        if (binding.religionSelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilReligion.isErrorEnabled = true
            binding.tilReligion.error = "Select Religion"
        }
        if (binding.religionSelector.text.toString() != "Atheism" && binding.religionSelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilCaste.isErrorEnabled = true
            binding.tilCaste.error = "Select Caste"
        }
        if (binding.motherTongueSelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilMotherTongue.isErrorEnabled = true
            binding.tilMotherTongue.error = "Select Religion"
        }
        if (binding.religionSelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilReligion.isErrorEnabled = true
            binding.tilReligion.error = "Select Religion"
        }
        if (binding.stateSelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilState.isErrorEnabled = true
            binding.tilState.error = "Select Religion"
        }
        if (binding.stateSelector.text.toString() != "Others" && binding.citySelector.text?.isBlank() == true) {
            isFilled = false
            binding.tilCity.isErrorEnabled = true
            binding.tilCity.error = "Select Religion"
        }

        if (binding.etMobile.text?.isBlank() == true) {
            isFilled = false
            binding.tilMobile.isErrorEnabled = true
            binding.tilMobile.error = "Enter Mobile No."
        }
        if (binding.etEmail.text?.isBlank() == true) {
            isFilled = false
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = "Enter email id"
        }
        if (binding.etPassword.text?.isBlank() == true) {
            isFilled = false
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "Enter password"
        }
        if (binding.etConfirmPass.text?.isBlank() == true) {
            isFilled = false
            binding.tilConfirmPass.isErrorEnabled = true
            binding.tilConfirmPass.error = "Enter Password"
        }

        binding.btnContinue.requestFocus()
        return isFilled
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