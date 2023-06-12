package com.example.matrimony.ui.mainscreen.meetingscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.UpcomingMeetingsAdapter
import com.example.matrimony.databinding.FragmentUpcomingMeetingsBinding
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UpcomingMeetingsFragment : Fragment() {

    lateinit var binding: FragmentUpcomingMeetingsBinding
    private var fragmentView: View? = null

    private val meetingsViewModel by activityViewModels<MeetingsViewModel>()
    private val albumViewModel by activityViewModels<AlbumViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView == null) {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_upcoming_meetings,
                container,
                false
            )
            fragmentView = binding.root

            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            meetingsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)
            loadUpcomingMeetings()
        }
        return fragmentView
    }

    private val editMeeting:(Int,Int,Int)->Unit={meetingId:Int,senderUserId:Int,receiverUserId:Int->
        val intent=Intent(requireContext(),ScheduleMeetingActivity::class.java)
        intent.putExtra("meeting_id",meetingId)
        intent.putExtra("sender_user_id",senderUserId)
        intent.putExtra("receiver_user_id",receiverUserId)

        startActivity(intent)

    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }


    private fun loadUpcomingMeetings() {
        Log.i(TAG, "comingMeet CurrUserId ${meetingsViewModel.userId}")
        val meetingRequestsRecyclerView = binding.rvUpcomingMeetings

        meetingRequestsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = UpcomingMeetingsAdapter(requireActivity(),meetingsViewModel,albumViewModel,editMeeting,viewFullProfile)
        meetingRequestsRecyclerView.adapter = adapter
        lifecycleScope.launch {

            meetingsViewModel.getMyMeetings(meetingsViewModel.userId, "UPCOMING")
                .observe(viewLifecycleOwner) {
                    if (it.isEmpty()) {
                        binding.noMeetingsMessage.visibility = View.VISIBLE
                        binding.rvUpcomingMeetings.visibility = View.GONE
                        return@observe
                    }
                    binding.noMeetingsMessage.visibility = View.GONE
                    binding.rvUpcomingMeetings.visibility = View.VISIBLE
                    adapter.setMeetingsList(it)
                }


        }
    }

}