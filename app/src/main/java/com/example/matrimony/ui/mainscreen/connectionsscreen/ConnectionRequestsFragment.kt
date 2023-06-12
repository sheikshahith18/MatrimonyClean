package com.example.matrimony.ui.mainscreen.connectionsscreen

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ConnectedProfilesAdapter
import com.example.matrimony.adapters.ConnectionRequestAdapter
import com.example.matrimony.databinding.FragmentConnectionRequestsBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ConnectionRequestsFragment : Fragment() {

    lateinit var binding: FragmentConnectionRequestsBinding
    private var fragmentView: View? = null

    private val connectionsViewModel by activityViewModels<ConnectionsViewModel>()
    private val albumViewModel by activityViewModels<AlbumViewModel>()

    private var adapter: ConnectionRequestAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView == null) {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_connection_requests,
                container,
                false
            )
            fragmentView=binding.root
            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            connectionsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)
            loadConnectionRequests()
        }
        return fragmentView
    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun loadConnectionRequests() {
        Log.i(TAG,"ConReqFrag CurrUserId ${connectionsViewModel.userId}")
        val connectedProfilesRecyclerView = binding.rvConnectionRequests
        connectedProfilesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter=ConnectionRequestAdapter(requireActivity(),connectionsViewModel,albumViewModel,viewFullProfile)
//        adapter = ConnectedProfilesAdapter(
//            connectionsViewModel,
//            onCallButtonClicked,
//            onRemoveButtonClicked
//        )
        connectedProfilesRecyclerView.adapter = adapter
        lifecycleScope.launch {
            connectionsViewModel.getConnectionRequests(connectionsViewModel.userId)
                .observe(requireActivity()) {
                    Log.i(TAG, "requested profiles count :${it.size}")
                    if (it.isEmpty()) {
                        adapter?.notifyDataSetChanged()
                        binding.noProfilesMessage.visibility = View.VISIBLE
                        binding.rvConnectionRequests.visibility = View.GONE
                        return@observe
                    }

                    binding.noProfilesMessage.visibility = View.GONE
                    binding.rvConnectionRequests.visibility = View.VISIBLE

                    val usersList = mutableListOf<UserData>()

                    lifecycleScope.launch {
                        it.forEach { userId ->
                            val userData = connectionsViewModel.getUserData(userId)
                            Log.i(TAG, userData.toString())
                            usersList.add(userData)
                        }
                        adapter?.setUsersList(usersList)
                    }

                }

        }
    }


}