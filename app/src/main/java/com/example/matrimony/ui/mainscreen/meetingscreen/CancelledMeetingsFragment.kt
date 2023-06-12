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
import com.example.matrimony.adapters.CompletedMeetingsAdapter
import com.example.matrimony.databinding.FragmentCompletedMeetingsBinding
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import kotlinx.coroutines.launch

class CancelledMeetingsFragment : Fragment() {
    lateinit var binding: FragmentCompletedMeetingsBinding
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
                R.layout.fragment_completed_meetings,
                container,
                false
            )
            fragmentView = binding.root

            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            meetingsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)
            loadMeetingRequests()
        }
        return fragmentView
    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }


    private fun loadMeetingRequests() {
        Log.i(TAG, "comingMeet CurrUserId ${meetingsViewModel.userId}")
        val meetingRequestsRecyclerView = binding.rvCompletedMeetings

        meetingRequestsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = CompletedMeetingsAdapter(requireContext(),meetingsViewModel,albumViewModel,this::class.java.simpleName,viewFullProfile)
        meetingRequestsRecyclerView.adapter = adapter
        lifecycleScope.launch {

            meetingsViewModel.getMyMeetings(meetingsViewModel.userId, "CANCELLED")
                .observe(viewLifecycleOwner) {
                    if (it.isEmpty()) {
                        binding.tvNoMeetingsDesc.text="Cancelled meetings will appear here"
                        binding.noMeetingsMessage.visibility = View.VISIBLE
                        binding.rvCompletedMeetings.visibility = View.GONE
                        return@observe
                    }
                    binding.noMeetingsMessage.visibility = View.GONE
                    binding.rvCompletedMeetings.visibility = View.VISIBLE
                    adapter.setMeetingsList(it)
                }


        }
    }
}