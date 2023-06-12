package com.example.matrimony.ui.mainscreen.meetingscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.matrimony.R
import com.example.matrimony.adapters.ViewPagerAdapter
import com.example.matrimony.databinding.FragmentMeetingBinding
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectedProfilesFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionRequestsFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MeetingsPageFragment : Fragment() {

    lateinit var binding: FragmentMeetingBinding

    private var fragmentView: View? = null
    private var upcomingMeetingsFragment: UpcomingMeetingsFragment? = null
    private var completedMeetingsFragment: CompletedMeetingsFragment? = null
    private var cancelledMeetingsFragment: CancelledMeetingsFragment? = null

    private val meetingsViewModel by activityViewModels<MeetingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meeting, container, false)
            fragmentView = binding.root
            setMeetingsViewPager()
        }
        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        binding.viewPagerMeetings.setCurrentItem(meetingsViewModel.currentSelectedPage, false)
    }

    private fun setMeetingsViewPager() {
        if (upcomingMeetingsFragment == null)
            upcomingMeetingsFragment = UpcomingMeetingsFragment()
        if (completedMeetingsFragment == null)
            completedMeetingsFragment = CompletedMeetingsFragment()
        if (cancelledMeetingsFragment == null)
            cancelledMeetingsFragment = CancelledMeetingsFragment()

        val viewPagerAdapter = ViewPagerAdapter(parentFragmentManager, lifecycle).apply {
            add(UpcomingMeetingsFragment())
            add(CompletedMeetingsFragment())
            add(CancelledMeetingsFragment())
        }
        binding.viewPagerMeetings.isSaveEnabled = false
        binding.viewPagerMeetings.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutMeetings,
            binding.viewPagerMeetings
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Upcoming"
                1 -> tab.text = "Completed"
                2 -> tab.text = "Cancelled"
            }
        }.attach()

        binding.viewPagerMeetings.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                meetingsViewModel.currentSelectedPage = position
            }
        })
    }

}

