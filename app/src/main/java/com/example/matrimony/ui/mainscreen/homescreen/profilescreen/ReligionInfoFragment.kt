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
import com.example.matrimony.databinding.FragmentReligionInfoBinding
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.editscreen.ReligionInfoEditActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReligionInfoFragment : Fragment() {

    lateinit var binding: FragmentReligionInfoBinding
    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()

    private var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (fragmentView == null) {


            binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_religion_info, container, false)
            fragmentView = binding.root
            initReligionDetails()
        }
        return fragmentView
    }

    private fun initReligionDetails() {
        lifecycleScope.launch {
            userProfileViewModel.getUser(userProfileViewModel.currentUserId)
                .observe(viewLifecycleOwner) {
                    binding.tvReligionDesc.text = it.religion
                    binding.tvCasteDesc.text = it.caste ?: "Not Set"
                    binding.tvZodiacDesc.text = it.zodiac
                    binding.tvStarDesc.text = it.star
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userProfileViewModel.userId == userProfileViewModel.currentUserId) {
            binding.imgBtnEdit.visibility = View.VISIBLE
            binding.imgBtnEdit.setOnClickListener {
                val intent = Intent(requireActivity(), ReligionInfoEditActivity::class.java)
                startActivity(intent)
            }
        }else
            binding.imgBtnEdit.visibility = View.GONE
    }

}