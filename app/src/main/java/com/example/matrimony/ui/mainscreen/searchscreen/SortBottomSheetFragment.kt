package com.example.matrimony.ui.mainscreen.searchscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.matrimony.R
import com.example.matrimony.databinding.BottomSheetSortBinding
import com.example.matrimony.models.SortOptions
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SortBottomSheetFragment() : BottomSheetDialogFragment() {

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val filterViewModel by activityViewModels<FilterViewModel>()

    lateinit var binding: BottomSheetSortBinding

    override fun onStart() {
        super.onStart()
//        changed = false
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_sort, container, false)
        initButtons()
        setCheckStatus()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        filterViewModel.selectedTab=binding.ascDescTabs.selectedTabPosition
    }

    override fun onResume() {
        super.onResume()
        binding.ascDescTabs.getTabAt(filterViewModel.selectedTab)?.select()
    }

    private fun setCheckStatus() {
        if (filterViewModel.nameSortFlag == 1)
            binding.radioGrpSort.check(R.id.radio_btn_name)
        else if (filterViewModel.ageSortFlag == 1)
            binding.radioGrpSort.check(R.id.radio_btn_age)
        else
            binding.radioGrpSort.check(R.id.radio_btn_date_created)


        if(filterViewModel.ascSortFlag==1)
//            binding.ascDescTabs.setSelectedTabIndicator(0)
        binding.ascDescTabs.getTabAt(0)?.select()
        else
        binding.ascDescTabs.getTabAt(1)?.select()

//        if (filterViewModel.ascSortFlag == 1)
//            binding.radioGrpSortType.check(R.id.radio_btn_ascending)
//        else
//            binding.radioGrpSortType.check(R.id.radio_btn_descending)

    }

    private fun initButtons() {

        val tabLayout=binding.ascDescTabs

        val ascTab:View=LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab_content,null)
        ascTab.findViewById<ImageView>(R.id.tab_icon).setImageResource(R.drawable.sort_ascending)
        ascTab.findViewById<TextView>(R.id.tab_text).text="Ascending"

        val descTab:View=LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab_content,null)
        descTab.findViewById<ImageView>(R.id.tab_icon).setImageResource(R.drawable.sort_descending)
        descTab.findViewById<TextView>(R.id.tab_text).text="Descending"
        val tab1:TabLayout.Tab=tabLayout.newTab()
        val tab2:TabLayout.Tab=tabLayout.newTab()
        tab1.customView = ascTab
        tab2.customView = descTab
        tabLayout.addTab(tab1)
        tabLayout.addTab(tab2)


        binding.imgBtnSave.setOnClickListener {

            val asc =
                when(binding.ascDescTabs.selectedTabPosition){
                    0->true
                    1->false
                    else->false
                }
//                when (binding.radioGrpSortType.checkedRadioButtonId) {
//                    R.id.radio_btn_ascending -> {
//                        true
//                    }
//                    R.id.radio_btn_descending -> {
//                        false
//                    }
//                    else ->
//                        false
//                }

            when (binding.radioGrpSort.checkedRadioButtonId) {
                R.id.radio_btn_name -> {
                    if (asc) filterViewModel.sortOptions = SortOptions.NAME_ASC
                    else filterViewModel.sortOptions = SortOptions.NAME_DESC
                    filterViewModel.nameSortFlag = 1
                    filterViewModel.ageSortFlag = 0
                    filterViewModel.idSortFlag = 0
                }
                R.id.radio_btn_age -> {
                    if (asc) filterViewModel.sortOptions = SortOptions.AGE_ASC
                    else filterViewModel.sortOptions = SortOptions.AGE_DESC
                    filterViewModel.nameSortFlag = 0
                    filterViewModel.ageSortFlag = 1
                    filterViewModel.idSortFlag = 0
                }
                R.id.radio_btn_date_created -> {
                    if (asc) filterViewModel.sortOptions = SortOptions.DATE_ASC
                    else filterViewModel.sortOptions = SortOptions.DATE_DESC

                    filterViewModel.nameSortFlag = 0
                    filterViewModel.ageSortFlag = 0
                    filterViewModel.idSortFlag = 1
                }
            }

            if(asc){
                filterViewModel.ascSortFlag=1
                filterViewModel.descSortFlag=0
            }else{
                filterViewModel.ascSortFlag=0
                filterViewModel.descSortFlag=1
            }

            filterViewModel.sortChange.value = true

            dismiss()
        }

        binding.imgBtnClose.setOnClickListener {
            dismiss()
        }


//        binding.radioGrpSort.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.radio_btn_name -> {
//                    if (filterViewModel.sortOptions == SortOptions.NAME_ASC || filterViewModel.sortOptions == SortOptions.NAME_DESC)
//                        return@setOnCheckedChangeListener
////                    Toast.makeText(requireContext(), "Name", Toast.LENGTH_SHORT).show()
//                    if (filterViewModel.ascSortFlag == 1)
//                        filterViewModel.sortOptions = SortOptions.NAME_ASC
//                    else
//                        filterViewModel.sortOptions = SortOptions.NAME_DESC
//
//                    filterViewModel.sortChange.value = true
//                    filterViewModel.nameSortFlag = 1
//                    filterViewModel.ageSortFlag = 0
//                    filterViewModel.idSortFlag = 0
//                }
//                R.id.radio_btn_age -> {
//                    if (filterViewModel.sortOptions == SortOptions.AGE_ASC || filterViewModel.sortOptions == SortOptions.AGE_DESC)
//                        return@setOnCheckedChangeListener
////                    Toast.makeText(requireContext(), "Age", Toast.LENGTH_SHORT).show()
//                    if (filterViewModel.ascSortFlag == 1)
//                        filterViewModel.sortOptions = SortOptions.AGE_ASC
//                    else
//                        filterViewModel.sortOptions = SortOptions.AGE_DESC
//
//                    filterViewModel.sortChange.value = true
//                    filterViewModel.nameSortFlag = 0
//                    filterViewModel.ageSortFlag = 1
//                    filterViewModel.idSortFlag = 0
//                }
//                R.id.radio_btn_date_created -> {
//                    if (filterViewModel.sortOptions == SortOptions.DATE_ASC || filterViewModel.sortOptions == SortOptions.DATE_DESC)
//                        return@setOnCheckedChangeListener
////                    Toast.makeText(requireContext(), "Date", Toast.LENGTH_SHORT).show()
//                    if (filterViewModel.ascSortFlag == 1)
//                        filterViewModel.sortOptions = SortOptions.DATE_ASC
//                    else
//                        filterViewModel.sortOptions = SortOptions.DATE_DESC
//
//                    filterViewModel.sortChange.value = true
//                    filterViewModel.nameSortFlag = 0
//                    filterViewModel.ageSortFlag = 0
//                    filterViewModel.idSortFlag = 1
//                }
//            }
//        }
//
//        binding.radioGrpSortType.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.radio_btn_ascending -> {
//                    if (filterViewModel.sortOptions == SortOptions.NAME_ASC || filterViewModel.sortOptions == SortOptions.AGE_ASC || filterViewModel.sortOptions == SortOptions.DATE_ASC)
//                        return@setOnCheckedChangeListener
////                    Toast.makeText(requireContext(), "Asc", Toast.LENGTH_SHORT).show()
//                    when (filterViewModel.sortOptions) {
//                        SortOptions.NAME_DESC -> {
//                            filterViewModel.sortOptions = SortOptions.NAME_ASC
//                            filterViewModel.sortChange.value = true
//                        }
//                        SortOptions.AGE_DESC -> {
//                            filterViewModel.sortOptions = SortOptions.AGE_ASC
//                            filterViewModel.sortChange.value = true
//                        }
//                        SortOptions.DATE_DESC -> {
//                            filterViewModel.sortOptions = SortOptions.DATE_ASC
//                            filterViewModel.sortChange.value = true
//                        }
//                        else -> {}
//                    }
//                    filterViewModel.ascSortFlag = 1
//                    filterViewModel.descSortFlag = 0
//                }
//                R.id.radio_btn_descending -> {
//                    if (filterViewModel.sortOptions == SortOptions.NAME_DESC || filterViewModel.sortOptions == SortOptions.AGE_DESC || filterViewModel.sortOptions == SortOptions.DATE_DESC)
//                        return@setOnCheckedChangeListener
////                    Toast.makeText(requireContext(), "Desc", Toast.LENGTH_SHORT).show()
//                    when (filterViewModel.sortOptions) {
//                        SortOptions.NAME_ASC -> {
//                            filterViewModel.sortOptions = SortOptions.NAME_DESC
//                            filterViewModel.sortChange.value = true
//                        }
//                        SortOptions.AGE_ASC -> {
//                            filterViewModel.sortOptions = SortOptions.AGE_DESC
//                            filterViewModel.sortChange.value = true
//                        }
//                        SortOptions.DATE_ASC -> {
//                            filterViewModel.sortOptions = SortOptions.DATE_DESC
//                            filterViewModel.sortChange.value = true
//                        }
//                        else -> {}
//                    }
//                    filterViewModel.sortChange.value = true
//                    filterViewModel.ascSortFlag = 0
//                    filterViewModel.descSortFlag = 1
//                }
//            }
//        }
//        binding.radioBtnName
//        binding.radioBtnAge
//        binding.radioBtnDateCreated
//        binding.radioBtnAscending
//        binding.radioBtnDescending

    }

}