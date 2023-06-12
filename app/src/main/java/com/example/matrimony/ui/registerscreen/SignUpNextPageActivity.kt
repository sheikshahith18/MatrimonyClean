package com.example.matrimony.ui.registerscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivitySignUpNextPageBinding
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpNextPageActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpNextPageBinding
    private val signUpViewModel by viewModels<SignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_next_page)

        binding.heightSelector.keyListener = null
        binding.physicalStatusSelector.keyListener = null
        binding.maritalStatusSelector.keyListener = null
        binding.childrenCountSelector.keyListener = null
        binding.educationSelector.keyListener = null
        binding.employedInSelector.keyListener = null
        binding.occupationSelector.keyListener = null
        binding.annualIncomeSelector.keyListener = null
        binding.familyStatusSelector.keyListener = null
        binding.familyTypeSelector.keyListener = null

        initHeightSpinner()
        initPhysicalStatusSpinner()
        initMaritalStatusSpinner()
        initEducationSpinner()
        initEmployedInSpinner()
        initOccupationSpinner()
        initAnnualIncomeSpinner()
        initFamilyStatusSpinner()
        initFamilyTypeSpinner()

        binding.btnCompleteSignup.setOnClickListener {
            completeSignUp()
        }
    }

    private fun completeSignUp() {
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return

        val userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        Log.i(TAG, "$userId")

        val height =
            if (binding.heightSelector.text.isNotBlank()) binding.heightSelector.text.toString() else "Not Set"
        val physicalStatus =
            if (binding.physicalStatusSelector.text.isNotBlank()) binding.physicalStatusSelector.text.toString() else "Not Set"
        val maritalStatus =
            if (binding.maritalStatusSelector.text.isNotBlank()) binding.maritalStatusSelector.text.toString() else "Not Set"
        val childrenCount = binding.childrenCountSelector.text.toString().toIntOrNull()
        val education =
            if (binding.educationSelector.text.isNotBlank()) binding.educationSelector.text.toString() else "Not Set"
        val employedIn =
            if (binding.employedInSelector.text.isNotBlank()) binding.employedInSelector.text.toString() else "Not Set"
        val occupation =
            if (binding.occupationSelector.text.isNotBlank()) binding.occupationSelector.text.toString() else "Not Set"
        val annualIncome =
            if (binding.annualIncomeSelector.text.isNotBlank()) binding.annualIncomeSelector.text.toString() else "Not Set"

        val familyStatus =
            if (binding.familyStatusSelector.text.isNotBlank()) binding.familyStatusSelector.text.toString() else "Not Set"
        val familyType =
            if (binding.familyTypeSelector.text.isNotBlank()) binding.familyTypeSelector.text.toString() else "Not Set"
        val about =
            if (binding.etAbout.text?.isNotBlank() == true) binding.etAbout.text.toString() else "Not Set"

        signUpViewModel.updateAdditionalDetails(
            userId, height, physicalStatus, maritalStatus, childrenCount, education, employedIn,
            occupation, annualIncome, familyStatus, familyType, about
        )

//        editor.putString(CURRENT_USER_ID,userId)
        lifecycleScope.launch {
            val intent = Intent(this@SignUpNextPageActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            Log.i(TAG,"useruser"+ signUpViewModel.getUser(userId).value.toString())
            delay(5000)
        }
    }


    private fun initHeightSpinner() {
        val heightArray = resources.getStringArray(R.array.height)
        val heightArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                heightArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = heightArray
                            filterResults.count = heightArray.size
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
        binding.heightSelector.setAdapter(heightArrayAdapter)

    }

    private fun initPhysicalStatusSpinner() {
        val physicalStatusArray = resources.getStringArray(R.array.physical_status)
        val physicalStatusArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                physicalStatusArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = physicalStatusArray
                            filterResults.count = physicalStatusArray.size
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
        binding.physicalStatusSelector.setAdapter(physicalStatusArrayAdapter)

    }

    private fun initMaritalStatusSpinner() {
        val maritalStatusArray = resources.getStringArray(R.array.marital_status)
        val maritalStatusArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                maritalStatusArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = maritalStatusArray
                            filterResults.count = maritalStatusArray.size
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
        binding.maritalStatusSelector.setAdapter(maritalStatusArrayAdapter)

        var selectedMaritalStatus: String
        var previousPosition = -1
        binding.maritalStatusSelector.setOnItemClickListener { parent, view, position, id ->
            if (previousPosition == position)
                return@setOnItemClickListener
            previousPosition = position
            signUpViewModel.maritalStatusPosition = position
            selectedMaritalStatus = parent.getItemAtPosition(position).toString()
            binding.childrenCountSelector.setText("")
            initChildrenCountSpinner(position)
            Log.i(TAG, selectedMaritalStatus)
        }
        if (signUpViewModel.maritalStatusPosition != -1)
            initChildrenCountSpinner(signUpViewModel.maritalStatusPosition)
    }

    private fun initChildrenCountSpinner(position: Int) {
        when (position) {
            0 -> {
                binding.tilChildrenCount.visibility = View.GONE
            }
            1, 2 -> {
                binding.tilChildrenCount.visibility = View.VISIBLE
                val countArray = resources.getStringArray(R.array.count_0_to_5)
                val countArrayAdapter = object : ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    countArray
                ) {
                    override fun getFilter(): Filter {
                        return object : Filter() {
                            override fun performFiltering(constraint: CharSequence?): FilterResults {
                                val filterResults = FilterResults()
                                filterResults.values = countArray
                                filterResults.count = countArray.size
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
                binding.childrenCountSelector.setAdapter(countArrayAdapter)
            }
        }

    }

    private fun initEducationSpinner() {
        val educationArray = resources.getStringArray(R.array.education)
        val educationArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                educationArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = educationArray
                            filterResults.count = educationArray.size
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
        binding.educationSelector.setAdapter(educationArrayAdapter)
    }

    private fun initEmployedInSpinner() {
        val employedInArray = resources.getStringArray(R.array.employed_in)
        val employedInArrayAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            employedInArray
        ) {
            override fun getFilter(): Filter {
                return object : Filter() {
                    override fun performFiltering(constraint: CharSequence?): FilterResults {
                        val filterResults = FilterResults()
                        filterResults.values = employedInArray
                        filterResults.count = employedInArray.size
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
        binding.employedInSelector.setAdapter(employedInArrayAdapter)
    }

    private fun initOccupationSpinner() {
        val occupationArray = resources.getStringArray(R.array.occupation)
        val occupationArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                occupationArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = occupationArray
                            filterResults.count = occupationArray.size
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
        binding.occupationSelector.setAdapter(occupationArrayAdapter)

    }

    private fun initAnnualIncomeSpinner() {
        val annualIncomeArray = resources.getStringArray(R.array.annual_income)
        val annualIncomeArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                annualIncomeArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = annualIncomeArray
                            filterResults.count = annualIncomeArray.size
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
        binding.annualIncomeSelector.setAdapter(annualIncomeArrayAdapter)

    }

    private fun initFamilyStatusSpinner() {
        val familyStatusArray = resources.getStringArray(R.array.family_status)
        val familyStatusArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                familyStatusArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = familyStatusArray
                            filterResults.count = familyStatusArray.size
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
        binding.familyStatusSelector.setAdapter(familyStatusArrayAdapter)
    }

    private fun initFamilyTypeSpinner() {
        val familyTypeArray = resources.getStringArray(R.array.family_type)
        val familyTypeArrayAdapter =
            object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                familyTypeArray
            ) {
                override fun getFilter(): Filter {
                    return object : Filter() {
                        override fun performFiltering(constraint: CharSequence?): FilterResults {
                            val filterResults = FilterResults()
                            filterResults.values = familyTypeArray
                            filterResults.count = familyTypeArray.size
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
        binding.familyTypeSelector.setAdapter(familyTypeArrayAdapter)
    }

}