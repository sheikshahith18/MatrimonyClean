package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.databinding.FragmentProfessionalInfoBinding
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen.ProfessionalInfoEditActivity
import kotlinx.coroutines.launch


class ProfessionalInfoFragment : Fragment() {

    lateinit var binding: FragmentProfessionalInfoBinding
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    private var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView == null) {


            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_professional_info,
                container,
                false
            )
            fragmentView = binding.root
            initProfessionalDetails()
        }
        return fragmentView
    }

    private fun initProfessionalDetails() {
        lifecycleScope.launch {
            userProfileViewModel.getUser(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) {
                    binding.tvEducationDesc.text = it.education.ifBlank { "-Not Set-" }
                    binding.tvEmployedInDesc.text = it.employed_in.ifBlank { "-Not Set-" }
                    binding.etOccupationDesc.text = it.occupation.ifBlank { "-Not Set-" }
                    binding.etAnnualIncomeDesc.text = it.annual_income.ifBlank { "-Not Set-" }
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userProfileViewModel.userId == userProfileViewModel.currentUserId) {
            binding.imgBtnEdit.visibility=View.VISIBLE
            binding.imgBtnEdit.setOnClickListener {
                val intent = Intent(requireActivity(), ProfessionalInfoEditActivity::class.java)
                startActivity(intent)
            }
        }else
            binding.imgBtnEdit.visibility=View.GONE

    }
}